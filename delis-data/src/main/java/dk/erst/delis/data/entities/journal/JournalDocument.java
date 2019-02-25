package dk.erst.delis.data.entities.journal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import dk.erst.delis.data.annotations.WebApiContent;
import dk.erst.delis.data.entities.AbstractCreateEntity;
import dk.erst.delis.data.entities.document.Document;
import dk.erst.delis.data.entities.organisation.Organisation;
import dk.erst.delis.data.enums.document.DocumentProcessStepType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@WebApiContent
public class JournalDocument extends AbstractCreateEntity {

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ORGANISATION_ID", nullable = false)
	private Organisation organisation;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "DOCUMENT_ID", nullable = false)
	private Document document;
	
	@Column(nullable = false, updatable = false)
	private boolean success;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private DocumentProcessStepType type;
	
	@Column(nullable = false)
	private String message;
	
	@Column(nullable = true)
	private Long durationMs;

}
