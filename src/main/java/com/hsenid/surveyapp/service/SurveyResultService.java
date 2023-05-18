package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.*;

import java.util.List;

public interface SurveyResultService {

    /**
     * add result
     *
     * @return a SurveyResultResponseDto
     */
    SurveyResultResponseDto addResult(SurveyResultRequestDto surveyResultRequestDto);

    /**
     * get survey result
     *
     * @return a SurveyResultListResponseDto
     */
    SurveyResultListResponseDto viewSurveyResults(String surveyId);

    /**
     * get user completed surveys
     *
     * @return a SurveyResultListResponseDto
     */
    List<SurveyResponseDto> viewUserCompletedSurveys(String userId);

    SurveyResultResponseDto viewUserSubmittedResult(final String surveyId, final String userId);

    SurveyResultListResponseDto getAllSurveys();

    QuestionAnswerResponseListDto getResponseData(SurveyResultListResponseDto surveyResultListResponseDto);
}
