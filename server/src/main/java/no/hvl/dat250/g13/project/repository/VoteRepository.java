package no.hvl.dat250.g13.project.repository;

import no.hvl.dat250.g13.project.domain.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Vote.VoteKey> {

    Iterable<Vote> findAllByIdVoterId(Long userId);

    Iterable<Vote> findAllByIdSurveyId(Long surveyId);
}
