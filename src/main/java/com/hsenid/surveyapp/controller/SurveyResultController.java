package com.hsenid.surveyapp.controller;

import com.hsenid.surveyapp.dto.*;
import com.hsenid.surveyapp.service.SurveyResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/survey-result")
public class SurveyResultController {

    @Autowired
    private SurveyResultService surveyResultService;

    /**
     * add survey result
     *
     * @param surveyResultRequestDto given SurveyResultRequestDto object
     * @return a {@link SurveyResultResponseDto} object
     */
    @PostMapping("/submit")
    public ResponseEntity<SurveyResultResponseDto> submitAnswer(@RequestBody final SurveyResultRequestDto surveyResultRequestDto) {
        SurveyResultResponseDto surveyResultResponseDto = surveyResultService.addResult(surveyResultRequestDto);
        return new ResponseEntity<>(surveyResultResponseDto, HttpStatus.CREATED);
    }

    /**
     * get survey
     *
     * @param surveyId given survey result id
     * @return a {@link QuestionResponseDto} object
     */
    @GetMapping("/{id}")
    public ResponseEntity<SurveyResultListResponseDto> viewSurveyResults(@PathVariable(value = "id") final String surveyId) {
        SurveyResultListResponseDto surveyResultListResponseDto = surveyResultService.viewSurveyResults(surveyId);
        return new ResponseEntity<>(surveyResultListResponseDto, HttpStatus.OK);
    }

    /**
     * get user complete surveys
     *
     * @param userId given user id
     * @return a {@link QuestionResponseDto} object
     */
    @GetMapping("assigned/{user-id}")
    public ResponseEntity<SurveyListResponseDto> viewUserCompletedSurveys(@PathVariable(value = "user-id") final String userId) {
        List<SurveyResponseDto> surveyResponseDtoList = surveyResultService.viewUserCompletedSurveys(userId);
        return new ResponseEntity(surveyResponseDtoList, HttpStatus.OK);
    }

    /**
     * get result for a survey submitted by a certain user
     *
     * @param surveyId given survey id
     * @param userId given user id
     * @return a {@link QuestionResponseDto} object
     */
    @GetMapping("submitted/{survey-id}/{user-id}")
    public ResponseEntity<SurveyResultResponseDto> viewUserSubmittedResult(@PathVariable(value = "survey-id") final String surveyId, @PathVariable(value = "user-id") final String userId) {
        SurveyResultResponseDto surveyResultResponseDto = surveyResultService.viewUserSubmittedResult(surveyId, userId);
        return new ResponseEntity<>(surveyResultResponseDto, HttpStatus.OK);
    }


    @GetMapping("/list")
    public ResponseEntity<SurveyResultListResponseDto> getSurveysWithQuestions() {
        SurveyResultListResponseDto surveyResultListResponseDtoList = surveyResultService.getAllSurveys();
        return new ResponseEntity(surveyResultListResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/listall")
    public QuestionAnswerResponseListDto getAllWithAnswers(){
        return surveyResultService.getResponseData(surveyResultService.getAllSurveys());
    }
}
