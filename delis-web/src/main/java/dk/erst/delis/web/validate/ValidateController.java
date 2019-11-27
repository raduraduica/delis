package dk.erst.delis.web.validate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dk.erst.delis.data.entities.document.Document;
import dk.erst.delis.data.enums.document.DocumentFormat;
import dk.erst.delis.task.document.parse.DocumentInfoService;
import dk.erst.delis.task.document.parse.DocumentInfoService.DocumentInfoData;
import dk.erst.delis.task.document.process.DocumentValidationTransformationService;
import dk.erst.delis.task.document.process.TransformationResultListener;
import dk.erst.delis.task.document.process.log.DocumentProcessLog;
import dk.erst.delis.task.document.process.log.DocumentProcessStep;
import dk.erst.delis.task.organisation.setup.data.OrganisationReceivingFormatRule;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ValidateController {

	private static final List<DocumentFormat> FORMAT_LIST = filterOut(DocumentFormat.values(), DocumentFormat.UNSUPPORTED);

	@Autowired
	private DocumentValidationTransformationService documentValidationTransformationService;

	@Autowired
	private DocumentInfoService documentInfoService;

	@RequestMapping("/validate/index")
	public String index(Model model, WebRequest webRequest) {

		fillModel(model);

		return "/validate/validate";
	}

	private static List<DocumentFormat> filterOut(DocumentFormat[] values, DocumentFormat exceptFormat) {
		return Arrays.asList(values).stream().filter(f -> f != exceptFormat).collect(Collectors.toList());
	}

	private void fillModel(Model model) {
		model.addAttribute("formatList", FORMAT_LIST);
	}

	@PostMapping("/validate/upload")
	public String upload(

			@RequestParam("files") MultipartFile[] files,

			@RequestParam(name = "validateFormat", required = false) String validateFormatName,

			Model model,

			WebRequest webRequest,

			RedirectAttributes redirectAttributes) {

		DocumentFormat ingoingDocumentFormat = null;
		if (!StringUtils.isEmpty(validateFormatName)) {
			ingoingDocumentFormat = DocumentFormat.valueOf(validateFormatName);
		}

		if (files == null || files.length == 0) {
			redirectAttributes.addFlashAttribute("errorMessage", "Please select at least one XML file");
			return "redirect:/validate/index";
		}

		List<ValidateResult> resultList = new ArrayList<ValidateResult>();
		
		List<String> fileNames = new ArrayList<String>();

		for (MultipartFile file : files) {

			ValidateResult result = new ValidateResult();

			File tempFile = null;
			try {
				result.setFileName(file.getOriginalFilename());
				fileNames.add(file.getOriginalFilename());
				
				tempFile = File.createTempFile("manual_upload_" + file.getName() + "_", ".xml");
				try (FileOutputStream fos = new FileOutputStream(tempFile)) {
					IOUtils.copy(file.getInputStream(), fos);
				}
				log.info("Saved file " + file.getOriginalFilename() + " as test file " + tempFile);
			} catch (IOException e) {
				log.error("Failed to save uploaded file to temp for " + file.getName(), e);
			}

			DocumentInfoData infoData = null;

			if (tempFile != null) {
				try {
					result.setFileSize(tempFile.length());
					if (ingoingDocumentFormat == null) {
						DocumentProcessStep step = DocumentProcessStep.buildDefineFormatStep();
						
						infoData = documentInfoService.documentInfoData(tempFile.toPath(), tempFile);
						DocumentFormat documentFormat = documentInfoService.defineDocumentFormat(infoData.getDocumentInfo());

						result.setDocumentFormat(documentFormat);
						result.setDocumentFormatDetected(true);
						
						tempFile = infoData.getFile();

						if (documentFormat.isUnsupported()) {
							DocumentProcessLog plog = new DocumentProcessLog();
							plog.setResultPath(tempFile.toPath());

							step.fillDefineFormatError(infoData.getDocumentInfo());
							
							plog.addStep(step);

							result.setProcessLog(plog);
							resultList.add(result);

							continue;
						}
					} else {
						result.setDocumentFormat(ingoingDocumentFormat);
					}

					Document document = new Document();
					document.setIngoingDocumentFormat(result.getDocumentFormat());

					OrganisationReceivingFormatRule receivingFormatRule = OrganisationReceivingFormatRule.OIOUBL;
					Path xmlLoadedPath = tempFile.toPath();
					TransformationResultListener transformationResultListener = new TransformationResultListener(null, null) {
						@Override
						public void notify(DocumentProcessLog plog, DocumentFormat resultFormat, File file) {
							/*
							 * Avoid notification - as we do not want to save documents into byte storage
							 */
						}
					};
					DocumentProcessLog plog = documentValidationTransformationService.process(document, xmlLoadedPath, receivingFormatRule, transformationResultListener);
					result.setProcessLog(plog);

					resultList.add(result);

				} finally {
					if (infoData != null) {
						deleteFile(infoData.getFile());
						deleteFile(infoData.getFileSbd());
					} else {
						deleteFile(tempFile);
					}
				}
			}
		}

		model.addAttribute("resultList", resultList);
		model.addAttribute("message", "Done validation of " + files.length + " uploaded XML files");
		fillModel(model);
		
		String customTitle = "";
		if (fileNames.size() > 1) {
			customTitle += fileNames.size() + " files: ";
		}
		customTitle += fileNames.stream().collect(Collectors.joining(", "));
		
		model.addAttribute("customTitle", customTitle);

		return "/validate/validate";
	}

	private void deleteFile(File f) {
		if (f != null) {
			if (!f.delete()) {
				f.deleteOnExit();
				log.info("Cannot delete file " + f + ", requested to delete on exit");
			} else {
				log.info("Successfully deleted file " + f);
			}
		}
	}

	@Getter
	@Setter
	private static class ValidateResult {

		private String fileName;
		private long fileSize;
		private DocumentFormat documentFormat;
		private boolean documentFormatDetected;
		private DocumentProcessLog processLog;

	}
}
