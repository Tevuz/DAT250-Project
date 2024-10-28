package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Survey;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.service.data.PollInfo;
import no.hvl.dat250.g13.project.service.data.SurveyInfo;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SurveyService {

    private SurveyRepository surveyRepository;

    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    /**
     * Create a survey.
     *
     * @param info {@link SurveyInfo} with data for the new survey
     * @return {@code Result<SurveyInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link SurveyInfo} if created</li>
     *          <li>{@link ServiceError} with {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing data </li>
     *      </ul>
     */
    public Result<SurveyInfo, ServiceError> createSurvey(SurveyInfo info) {
        if (info.polls().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Polls was not provided"));
        if (info.polls().orElse(List.of()).stream().anyMatch(poll -> Strings.isBlank(poll.text().orElse(""))))
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Poll text was not provided"));
        if (info.polls().orElse(List.of()).stream().anyMatch(poll -> poll.options().isEmpty()))
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Options for poll was not provided"));
        if (info.polls().orElse(List.of()).stream().flatMap(poll -> poll.options().stream()).anyMatch(option -> Strings.isBlank(option.text().orElse(""))))
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Option text was not provided"));

        Survey survey = info.into();
        return new Result.Ok<>(new SurveyInfo(surveyRepository.save(survey)));
    }


    /**
     * Update a survey.
     *
     * @param info {@link SurveyInfo} with data to replace the survey with id=={@code info.id}
     * @return {@code Result<SurveyInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link SurveyInfo} if updated</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing survey id </li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if survey with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<SurveyInfo, ServiceError> updateSurvey(SurveyInfo info) {
        if (info.id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Survey id was not provided"));

        Optional<Survey> optional = surveyRepository.findById(info.id().get());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Survey does not exist"));

        Survey survey = optional.get();
        survey.setTitle(info.title().orElse(""));
        survey.setPolls(info.polls().orElse(List.of()).stream().map(PollInfo::into).toList());
        return new Result.Ok<>(new SurveyInfo(surveyRepository.save(survey)));
    }

    /**
     * Read a survey.
     *
     * @param info {@link SurveyInfo} with id for the survey to read
     * @return {@code Result<SurveyInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link SurveyInfo} if found</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing survey id </li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if survey with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<SurveyInfo, ServiceError> readSurveyById(SurveyInfo info) {
        if (info.id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Survey id was not provided"));

        Optional<Survey> optional = surveyRepository.findById(info.id().get());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Survey does not exist"));

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
        return new Result.Ok<>(Streamable.of(surveys).map(SurveyInfo::new));
    }

    /*
        Todo: implement
        public Result<Iterable<SurveyInfo>, ServiceError> readRecentSurveys(Timestamp after) {
        }
    */

    /*
        Todo: implement
        public Result<Iterable<SurveyInfo>, ServiceError> readTrendingSurveys() {
        }
    */

    /**
     * Delete a survey.
     *
     * @param info
     * @return Result&lt;SurveyInfo, ServiceError&gt;
     *      <ul>
     *          <li>{@link SurveyInfo null} if deleted successfully</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing survey id </li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if survey with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<SurveyInfo, ServiceError> deleteSurvey(SurveyInfo info) {
        if (info.id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Survey id was not provided"));
        if (!surveyRepository.existsById(info.id().get()))
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Survey does not exist"));

        surveyRepository.deleteById(info.id().get());
        return new Result.Ok<>(null);
    }
}
