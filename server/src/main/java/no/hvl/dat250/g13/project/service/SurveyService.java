package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Survey;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.survey.SurveyCreate;
import no.hvl.dat250.g13.project.service.data.survey.SurveyId;
import no.hvl.dat250.g13.project.service.data.survey.SurveyInfo;
import no.hvl.dat250.g13.project.service.data.survey.SurveyUpdate;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final VoteRepository voteRepository;

    public SurveyService(SurveyRepository surveyRepository, VoteRepository voteRepository) {
        this.surveyRepository = surveyRepository;
        this.voteRepository = voteRepository;
    }

    /**
     * Create a survey.
     *
     * @param info {@link SurveyCreate} with data for the new survey
     * @return {@code Result<SurveyInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link SurveyInfo} if created</li>
     *      </ul>
     */
    public Result<SurveyInfo, ServiceError> createSurvey(SurveyCreate info) {
        Survey survey = info.into();
        return new Result.Ok<>(new SurveyInfo(surveyRepository.save(survey)));
    }


    /**
     * Update a survey.
     *
     * @param info {@link SurveyUpdate} with data to replace the survey with value=={@code info.value}
     * @return {@code Result<Void, ServiceError>}:
     *      <ul>
     *          <li>{@link Void} if updated</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if survey does not exist</li>
     *      </ul>
     */
    public Result<Void, ServiceError> updateSurvey(SurveyUpdate info) {
        Optional<Survey> optional = surveyRepository.findById(info.id());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Survey does not exist"));

        Survey survey = info.apply(optional.get());
        survey = surveyRepository.save(survey);
        return new Result.Ok<>(null);
    }

    /**
     * Read a survey.
     *
     * @param info {@link SurveyId} with id for the survey to read
     * @return {@code Result<SurveyInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link SurveyInfo} if found</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if survey does not exist</li>
     *      </ul>
     */
    public Result<SurveyInfo, ServiceError> readSurveyById(SurveyId info) {
        Optional<Survey> optional = surveyRepository.findById(info.id());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Survey does not exist"));

        optional.ifPresent(survey -> survey.setVoteTotal(voteRepository.countBySurvey(survey.getId())));
        optional.stream()
                .flatMap(survey -> survey.getPolls().stream())
                .flatMap(poll -> poll.getOptions().stream())
                .forEach(option -> option.setVoteCount(voteRepository.countByOption(option.getId())));

        return new Result.Ok<>(new SurveyInfo(optional.get()));
    }

    /**
     * Read all surveys.
     *
     * @return {@code Result<SurveyInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link SurveyInfo} list of all surveys</li>
     *      </ul>
     */
    public Result<Iterable<SurveyInfo>, ServiceError> readAllSurveys() {
        var surveys = surveyRepository.findAll();
        Streamable.of(surveys).forEach(survey -> survey.setVoteTotal(voteRepository.countBySurvey(survey.getId())));
        Streamable.of(surveys).stream()
                .flatMap(survey -> survey.getPolls().stream())
                .flatMap(poll -> poll.getOptions().stream())
                .forEach(option -> option.setVoteCount(voteRepository.countByOption(option.getId())));

        return new Result.Ok<>(Streamable.of(surveys).map(SurveyInfo::new).toList());
    }

    /*
        Todo: implement
        public Result<Iterable<SurveyDTO.Info>, ServiceError> readRecentSurveys(Timestamp after) {
        }
    */

    /*
        Todo: implement
        public Result<Iterable<SurveyDTO.Info>, ServiceError> readTrendingSurveys() {
        }
    */

    /**
     * Delete a survey.
     *
     * @param info {@link SurveyId} with value for the survey to delete
     * @return {@code Result<Void, ServiceError>}:
     *      <ul>
     *          <li>{@link Void} if deleted successfully</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if survey does not exist</li>
     *      </ul>
     */
    public Result<Void, ServiceError> deleteSurvey(SurveyId info) {
        if (!surveyRepository.existsById(info.id()))
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Survey does not exist"));

        surveyRepository.deleteById(info.id());
        return new Result.Ok<>(null);
    }
}
