package dk.erst.delis.persistence.organization;

import dk.erst.delis.data.entities.organisation.Organisation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Iehor Funtusov, created by 04.01.19
 */

@Repository
public interface OrganizationRepository extends JpaRepository<Organisation, Long> { }
