package no.hvl.dat250.g13.project.repository;

import no.hvl.dat250.g13.project.domain.Identifiers.SurveyKey;
import no.hvl.dat250.g13.project.domain.Identifiers.UserKey;
import no.hvl.dat250.g13.project.domain.Identifiers.VoteKey;
import no.hvl.dat250.g13.project.domain.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends CrudRepository<Vote, VoteKey> {

    Iterable<Vote> findAllByUserId(UserKey userId);

    Iterable<Vote> findAllBySurveyId(SurveyKey surveyId);

    boolean existsByUserIdAndSurveyId(UserKey userId, SurveyKey surveyId);

    default boolean existsBy(UserKey userId, SurveyKey surveyId) {
        return existsByUserIdAndSurveyId(userId, surveyId);
    }


    Iterable<Vote> findAllByUserIdAndSurveyId(UserKey userId, SurveyKey surveyId);
    default Iterable<Vote> findAllBy(UserKey userId, SurveyKey surveyId) {
        return findAllByUserIdAndSurveyId(userId, surveyId);
    }
}
