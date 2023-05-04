package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.QuestionResponseDto;
import com.hsenid.surveyapp.dto.SurveyRequestDto;
import com.hsenid.surveyapp.dto.SurveyResponseDto;
import com.hsenid.surveyapp.dto.SurveyResultListResponseDto;
import com.hsenid.surveyapp.model.Question;

import java.util.List;

public interface SurveyService {

    /**
     * add survey
     *
     * @return a SurveyResponseDto
     */
    SurveyResponseDto createSurvey(SurveyRequestDto surveyRequestDto);

    /**
     * update survey
     *
     * @return a SurveyResponseDto
     */
    SurveyResponseDto updateSurvey(String surveyId, SurveyRequestDto surveyRequestDto);

    /**
     * get survey list with questions
     *
     * @return a list ofSurveyResponseDto
     */
    List<SurveyResponseDto> getAllSurveysWithQuestions();

    /**
     * get surveys assigned to users
     *
     * @return a list of SurveyResponseDto
     */
    List<SurveyResponseDto> getUserAssignedSurveys(String userId);

    /**
     * get survey by id
     *
     * @return a SurveyResponseDto
     */
    SurveyResponseDto getSurveyById(String surveyId);

    /**
     * delete survey
     */
    void deleteSurveyById(String surveyId);
}
