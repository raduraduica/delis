package dk.erst.delis.task.identifier.load.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import dk.erst.delis.data.entities.identifier.Identifier;
import org.junit.Test;

import dk.erst.delis.TestUtil;

public class CSVIdentifierStreamReaderTest {

	@Test
	public void testLoadCSV() {
		InputStream is = TestUtil.getResourceByClass(getClass(), "LF_ISO88591.csv");
		assertNotNull(is);

		CSVIdentifierStreamReader r = new CSVIdentifierStreamReader(is, StandardCharsets.ISO_8859_1, ';');
		
		int count = 0;
		while (r.hasNext()) {
			Identifier i = r.next();
			count++;
			
			assertNotNull(i.getValue());
		}
		
		assertEquals(10, count);
	}

}
