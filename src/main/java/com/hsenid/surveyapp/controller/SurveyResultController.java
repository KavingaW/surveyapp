package com.hsenid.surveyapp.controller;

import com.hsenid.surveyapp.dto.*;
import com.hsenid.surveyapp.service.SurveyResultService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//testController
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SurveyListResponseDto> viewUserCompletedSurveys(@PathVariable(value = "user-id") final String userId) {
        List<SurveyResponseDto> surveyResponseDtoList = surveyResultService.viewUserCompletedSurveys(userId);
        return new ResponseEntity(surveyResponseDtoList, HttpStatus.OK);
    }

    /**
     * get result for a survey submitted by a certain user
     *
     * @param surveyId given survey id
     * @param userId   given user id
     * @return a {@link QuestionResponseDto} object
     */
    @GetMapping("submitted/{survey-id}/{user-id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SurveyResultResponseDto> viewUserSubmittedResult(@PathVariable(value = "survey-id") final String surveyId, @PathVariable(value = "user-id") final String userId) {
        SurveyResultResponseDto surveyResultResponseDto = surveyResultService.viewUserSubmittedResult(surveyId, userId);
        return new ResponseEntity<>(surveyResultResponseDto, HttpStatus.OK);
    }


    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<SurveyResponseDto>> getSurveysWithQuestions() {
        List<SurveyResponseDto> surveyResponseDto = surveyResultService.getAllSurveys();
        return new ResponseEntity(surveyResponseDto, HttpStatus.OK);
    }

    @GetMapping("/listall/{survey-id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataForChartGeneration> getAllWithAnswers(@PathVariable(value = "survey-id") final String surveyId) {

        SurveyAnswerResponseDto surveyAnswerResponseDto = surveyResultService.getResponseData(surveyId);

        return new ResponseEntity(DataForChartGeneration.builder().surveyResult(surveyAnswerResponseDto).build(), HttpStatus.OK);
    }


//    @GetMapping("/special")
//    public ResponseEntity<DataForChart> getDtaForChart() {
//        List<QuestionResponse> questionResponseList = new ArrayList<>();
//
//        QuestionResponse questionResponse1 = new QuestionResponse();
//        questionResponse1.setId("644b614cf13caf21800a7e0a");
//        questionResponse1.setText("Are You a Tennager");
//        Map<String, Integer> answerResponse1 = new HashMap<>();
//        answerResponse1.put("Y", 2);
//        answerResponse1.put("N", 2);
//        questionResponse1.setResponseAnswerMap(answerResponse1);
//
//        QuestionResponse questionResponse2 = new QuestionResponse();
//        questionResponse2.setId("644b5479198fa618ad226f77");
//        questionResponse2.setText("Please Enter Gender ?");
//        Map<String, Integer> answerResponse2 = new HashMap<>();
//        answerResponse2.put("MALE", 3);
//        answerResponse2.put("FEMALE", 1);
//        questionResponse2.setResponseAnswerMap(answerResponse2);
//
//        questionResponseList.add(questionResponse1);
//        questionResponseList.add(questionResponse2);
//
//        SurveyResult surveyResult = new SurveyResult();
//        surveyResult.setId("6466ff3b80cb0f6abe54bd75");
//        surveyResult.setSurveyName("Survey 01");
//        surveyResult.setQuestionResponse(questionResponseList);
//        surveyResult.setTotResponses(4);
//        return new ResponseEntity(DataForChart.builder().surveyResult(surveyResult).build(), HttpStatus.OK);
//    }

}




