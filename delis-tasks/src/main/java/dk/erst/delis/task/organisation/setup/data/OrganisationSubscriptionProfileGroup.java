package dk.erst.delis.task.organisation.setup.data;


/*
 * Copied from https://www.galaxygw.com/peppol-documents/ 
 * 
 * which references https://github.com/OpenPEPPOL/documentation/tree/master/TransportInfrastructure
 */
public enum OrganisationSubscriptionProfileGroup {

	CII("urn:fdc:peppol.eu:2017:poacc:billing:01:1.0",
			
			new String[] {
					
					"urn:un:unece:uncefact:data:standard:CrossIndustryInvoice:100::CrossIndustryInvoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::D16B"
			}),
	
	/*
	 * PEPPOL BIS Billing - Post-Award Coordinating Community version 3.0.2
	 * 
	 * http://docs.peppol.eu/poacc/billing/3.0/bis/
	 */
	
	BIS3("urn:fdc:peppol.eu:2017:poacc:billing:01:1.0",
			
			new String[] {
			
			"urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1", 
	
			"urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1"
	
			}),
	
	OIOUBL("urn:www.nesubl.eu:profiles:profile5:ver2.0", 
			
			new String[] {
					
			"urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##OIOUBL-2.02::2.0",
			
			"urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##OIOUBL-2.02::2.0"
			
			}),
	
	/*
	 * BIS Invoice Response 3.0
	 * 
	 * http://docs.peppol.eu/poacc/upgrade-3/profiles/63-invoiceresponse/
	 */
	
	BisInvoiceResponse30("urn:fdc:peppol.eu:poacc:bis:invoice_response:3", 
			
			new String[] {
					
			"urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2::ApplicationResponse##urn:fdc:peppol.eu:poacc:trns:invoice_response:3::2.1"
					
			}),	
	
	/*
	 * BIS Invoice Response 3.0
	 * 
	 * http://docs.peppol.eu/poacc/upgrade-3/profiles/36-mlr/
	 */
	
	MessageLevelResponse30("urn:fdc:peppol.eu:poacc:bis:mlr:3", 
			
			new String[] {
					
			"urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2::ApplicationResponse##urn:fdc:peppol.eu:poacc:trns:mlr:3::2.1"
					
			}),		
	
	;
	
	public static String DEFAULT_PROCESS_SCHEME_ID = "urn:fdc:peppol.eu:2017:identifiers:proc-id";
	
	private final String processId;
	private final String[] documentIdentifiers;
	
	private OrganisationSubscriptionProfileGroup(String processId, String[] documentIdentifiers) {
		this.processId = processId;
		this.documentIdentifiers = documentIdentifiers;
	}

	public String[] getDocumentIdentifiers() {
		return documentIdentifiers;
	}
	
	public String getCode() {
		return this.name();
	}
	
	public String getProcessType() {
		return DEFAULT_PROCESS_SCHEME_ID;
	}
	
	public String getProcessSchemeSMP() {
		if (this == OIOUBL) {
			return "nes-procid-ubl";
		}
		return "cenbii-procid-ubl";
	}
	
	public String getProcessId() {
		return this.processId;
	}
}
