package dk.erst.delis.task.document.process.validate.result;

import dk.erst.delis.data.DocumentFormat;
import dk.erst.delis.data.DocumentFormatFamily;

public class SchematronResultCollectorFactory {

	public static ISchematronResultCollector getCollector(DocumentFormat df) {
		if (df.getDocumentFormatFamily() == DocumentFormatFamily.OIOUBL) {
			return OIOUBLSchematronResultCollector.INSTANCE;
		}
		return BIS3SchematronResultCollector.INSTANCE;
	}
}