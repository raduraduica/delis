package dk.erst.delis.web.document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dk.erst.delis.dao.DocumentRepository;
import dk.erst.delis.dao.JournalDocumentRepository;
import dk.erst.delis.data.Document;
import dk.erst.delis.task.document.load.DocumentLoadService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DocumentController {

	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private JournalDocumentRepository journalDocumentRepository;

	@Autowired
	private DocumentLoadService documentLoadService;

	@RequestMapping("/document/list")
	public String list(Model model) {
		return listFilter(model);
	}

	@PostMapping("/document/list/filter")
	public String listFilter(Model model) {
		List<Document> list;
		list = documentRepository.findAll(PageRequest.of(0, 10, Sort.by("id").descending())).getContent();
		model.addAttribute("documentList", list);
		return "/document/list";
	}

	@GetMapping("/document/view/{id}")
	public String view(@PathVariable long id, Model model, RedirectAttributes ra) {
		Document document = documentRepository.findById(id).get();
		if (document == null) {
			ra.addFlashAttribute("errorMessage", "Document is not found");
			return "redirect:/home";
		}

		model.addAttribute("document", document);
		model.addAttribute("lastJournalList", journalDocumentRepository.findByDocumentOrderByIdDesc(document));

		return "/document/view";
	}

	@PostMapping("/document/upload")
	public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		if (file == null || file.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "File is empty");
		} else {

			File tempFile = null;
			try {
				tempFile = File.createTempFile("manual_upload_" + file.getName() + "_", ".xml");
				try (FileOutputStream fos = new FileOutputStream(tempFile)) {
					IOUtils.copy(file.getInputStream(), fos);
				}
			} catch (IOException e) {
				log.error("Failed to save uploaded file to temp for " + file.getName(), e);
			}
			if (tempFile != null) {
				log.info("Created test file " + tempFile);
				Document document = documentLoadService.loadFile(tempFile.toPath());
				if (document == null) {
					redirectAttributes.addFlashAttribute("errorMessage", "Cannot load file " + tempFile + " as document, see logs");
				} else {
					if (document.getDocumentStatus().isLoadFailed()) {
						redirectAttributes.addFlashAttribute("errorMessage", "Uploaded file as a document with status " + document.getDocumentStatus());
					} else {
						redirectAttributes.addFlashAttribute("message", "Successfully uploaded file as a document with status " + document.getDocumentStatus());
					}
					return "redirect:/document/view/"+document.getId();
				}
			}
		}

		return "redirect:/document/list";
	}

}
