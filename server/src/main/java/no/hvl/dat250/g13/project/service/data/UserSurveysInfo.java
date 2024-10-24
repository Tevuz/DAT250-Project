package no.hvl.dat250.g13.project.service.data;

import java.util.List;

public record UserSurveysInfo(
        Long id,
        List<SurveyInfo> surveys
) {
}
