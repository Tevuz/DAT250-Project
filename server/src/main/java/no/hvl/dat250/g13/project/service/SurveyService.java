package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Survey;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.service.data.SurveyDTO;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    /**
     * Create a survey.
     *
     * @param info {@link SurveyDTO.Info} with data for the new survey
     * @return {@code Result<SurveyDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link SurveyDTO.Info} if created</li>
     *      </ul>
     */
    public Result<SurveyDTO.Info, ServiceError> createSurvey(SurveyDTO.Create info) {
        Survey survey = info.into();
        return new Result.Ok<>(new SurveyDTO.Info(surveyRepository.save(survey)));
    }


    /**
     * Update a survey.
     *
     * @param info {@link SurveyDTO.Info} with data to replace the survey with value=={@code info.value}
     * @return {@code Result<SurveyDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link SurveyDTO.Info} if updated</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if survey with {@code info.value} does not exist </li>
     *      </ul>
     */
    public Result<SurveyDTO.Info, ServiceError> updateSurvey(SurveyDTO.Update info) {
        Optional<Survey> optional = surveyRepository.findById(info.id());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Survey does not exist"));

        Survey survey = info.apply(optional.get());
        return new Result.Ok<>(new SurveyDTO.Info(surveyRepository.save(survey)));
    }

    /**
     * Read a survey.
     *
     * @param info {@link SurveyDTO.Info} with value for the survey to read
     * @return {@code Result<SurveyDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link SurveyDTO.Info} if found</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if survey with {@code info.value} does not exist </li>
     *      </ul>
     */
    public Result<SurveyDTO.Info, ServiceError> readSurveyById(SurveyDTO.Id info) {
        Optional<Survey> optional = surveyRepository.findById(info.id());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Survey does not exist"));

        return new Result.Ok<>(new SurveyDTO.Info(optional.get()));
    }


    /**
     * Read all surveys.
     *
     * @return {@code Result<SurveyDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link SurveyDTO.Info} list of all surveys</li>
     *      </ul>
     */
    public Result<Iterable<SurveyDTO.Info>, ServiceError> readAllSurveys() {
        var surveys = surveyRepository.findAll();
        return new Result.Ok<>(Streamable.of(surveys).map(SurveyDTO.Info::new));
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
     * @param info {@link SurveyDTO.Id}  with value for the survey to delete
     * @return Result&lt;SurveyDTO.Info, ServiceError&gt;
     *      <ul>
     *          <li>{@link Object null} if deleted successfully</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if survey with {@code info.value} does not exist </li>
     *      </ul>
     */
    public Result<Object, ServiceError> deleteSurvey(SurveyDTO.Id info) {
        if (!surveyRepository.existsById(info.id()))
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Survey does not exist"));

        surveyRepository.deleteById(info.id());
        return new Result.Ok<>(null);
    }
}
