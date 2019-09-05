package dk.erst.delis.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class JournalDocumentErrorDaoRepositoryTest {

	@Autowired
	private JournalDocumentErrorDaoRepository journalDocumentErrorDaoRepository;
	
	@Test
	public void testGetErrorStatByErrorId() {
		journalDocumentErrorDaoRepository.loadDocumentByErrorId(0L);
		journalDocumentErrorDaoRepository.findErrorStatByErrorId(0L);
	}

}
