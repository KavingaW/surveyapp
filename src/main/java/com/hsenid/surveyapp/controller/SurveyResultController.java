package com.hsenid.surveyapp.controller;

import com.hsenid.surveyapp.dto.QuestionResponseDto;
import com.hsenid.surveyapp.dto.SurveyResultListResponseDto;
import com.hsenid.surveyapp.dto.SurveyResultRequestDto;
import com.hsenid.surveyapp.dto.SurveyResultResponseDto;
import com.hsenid.surveyapp.service.SurveyResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
