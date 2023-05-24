package com.hsenid.surveyapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResult {
    String id;
    String surveyName;
    List<QuestionResponse> questionResponse;
    Integer totResponses;

}