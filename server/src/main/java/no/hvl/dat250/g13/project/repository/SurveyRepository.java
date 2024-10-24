package no.hvl.dat250.g13.project.repository;

import no.hvl.dat250.g13.project.domain.Survey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long> {
}
