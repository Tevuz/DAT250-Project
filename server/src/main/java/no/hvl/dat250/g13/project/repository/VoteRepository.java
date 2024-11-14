package no.hvl.dat250.g13.project.repository;

import no.hvl.dat250.g13.project.domain.VoteKey;
import no.hvl.dat250.g13.project.domain.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends CrudRepository<Vote, VoteKey> {

    Iterable<Vote> findAllByUserId(Long userId);

    Iterable<Vote> findAllBySurveyId(Long surveyId);

    Iterable<Vote> findAllByOptionId(Long optionId);

    boolean existsByUserIdAndSurveyId(Long userId, Long surveyId);

    Iterable<Vote> findAllByUserIdAndSurveyId(Long userId, Long surveyId);

    long countDistinctByUserIdAndSurveyId(Long userId, Long surveyId);
    long countDistinctByUserIdAndOptionId(Long userId, Long optionId);

    default long countBySurvey(Long surveyId) {
        var votes = findAllBySurveyId(surveyId);
        return Streamable.of(votes).stream()
                .map(vote -> countDistinctByUserIdAndSurveyId(vote.getUserId(), surveyId))
                .reduce(0L, Long::sum);
    }

    default long countByOption(Long optionId) {
        var votes = findAllByOptionId(optionId);
        return Streamable.of(votes).stream()
                .map(vote -> countDistinctByUserIdAndOptionId(vote.getUserId(), optionId))
                .reduce(0L, Long::sum);
    }
}
