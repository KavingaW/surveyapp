package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.SurveyResultListResponseDto;
import com.hsenid.surveyapp.dto.SurveyResultRequestDto;
import com.hsenid.surveyapp.dto.SurveyResultResponseDto;

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
}
