package com.hsenid.surveyapp.dto;

import com.hsenid.surveyapp.model.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class SurveyResultRequestDto {

    private String userId;
    private String surveyId;
    private Map<String, String> questionMap;
}
