package dk.erst.delis.task.document.parse;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.erst.delis.data.DocumentFormat;
import dk.erst.delis.task.document.parse.data.DocumentInfo;
import dk.erst.delis.task.document.parse.data.DocumentInfoRootTag;

public class DocumentFormatDetectServiceTest {

	@Test
	public void testDefineDocumentFormat() {
		DocumentFormatDetectService s = new DocumentFormatDetectService();
		assertEquals(DocumentFormat.UNSUPPORTED, s.defineDocumentFormat(null));
		assertEquals(DocumentFormat.UNSUPPORTED, s.defineDocumentFormat(new DocumentInfo()));
	
		assertEquals(DocumentFormat.UNSUPPORTED, s.defineDocumentFormat(tc("CrossIndustryInvoice", "wrong", null)));
		assertEquals(DocumentFormat.UNSUPPORTED, s.defineDocumentFormat(tc("wrong", "urn:un:unece:uncefact:data:standard:CrossIndustryInvoice:100", null)));
		assertEquals(DocumentFormat.UNSUPPORTED, s.defineDocumentFormat(tc("Invoice", "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2", null)));
		assertEquals(DocumentFormat.UNSUPPORTED, s.defineDocumentFormat(tc("Wrong", "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2", "OIOUBL-2.02")));
		
		assertEquals(DocumentFormat.CII, s.defineDocumentFormat(tc("CrossIndustryInvoice", "urn:un:unece:uncefact:data:standard:CrossIndustryInvoice:100", null)));
		
		assertEquals(DocumentFormat.BIS3_CREDITNOTE, s.defineDocumentFormat(tc("CreditNote", "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2", "anything")));
		assertEquals(DocumentFormat.BIS3_INVOICE, s.defineDocumentFormat(tc("Invoice", "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2", "anything")));

		assertEquals(DocumentFormat.OIOUBL_CREDITNOTE, s.defineDocumentFormat(tc("CreditNote", "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2", "OIOUBL-2.02")));
		assertEquals(DocumentFormat.OIOUBL_INVOICE, s.defineDocumentFormat(tc("Invoice", "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2", "OIOUBL-2.02")));
	}

	private DocumentInfo tc(String rootTag, String namespace, String customizationId) {
		DocumentInfo di;
		DocumentInfoRootTag root;
		di = new DocumentInfo();
		root = new DocumentInfoRootTag();
		di.setRoot(root);
		root.setRootTag(rootTag);
		root.setNamespace(namespace);
		
		di.setCustomizationID(customizationId);
		
		return di;
	}

}
