package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.QuestionRequestDto;
import com.hsenid.surveyapp.dto.QuestionResponseDto;

import java.util.List;

public interface QuestionService {

    /**
     * create question
     *
     * @return a QuestionResponseDto
     */
    QuestionResponseDto createQuestion(String surveyI,QuestionRequestDto questionRequestDto);

    /**
     * update question
     *
     * @return a QuestionResponseDto
     */
    QuestionResponseDto updateQuestion(QuestionRequestDto questionRequestDto, String id);

    /**
     * delete question
     */
    void deleteQuestion(String questionId);

    /**
     * get question by id
     *
     * @return a QuestionResponseDto
     */
    QuestionResponseDto getQuestionById(String questionId);

    /**
     * get question list
     *
     * @return list of {@link QuestionResponseDto}
     */
    List<QuestionResponseDto> getQuestions();

    /**
     * get question by type
     *
     * @return a list of QuestionResponseDto
     */
    List<QuestionResponseDto> getQuestionsByType(String type);
}
