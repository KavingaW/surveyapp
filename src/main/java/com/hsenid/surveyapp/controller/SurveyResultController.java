package com.hsenid.surveyapp.controller;

import com.hsenid.surveyapp.dto.SurveyResultListResponseDto;
import com.hsenid.surveyapp.dto.SurveyResultRequestDto;
import com.hsenid.surveyapp.dto.SurveyResultResponseDto;
import com.hsenid.surveyapp.service.SurveyResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/survey-result")
public class SurveyResultController {

    @Autowired
    private SurveyResultService surveyResultService;

    @PostMapping("/submit")
    public ResponseEntity<SurveyResultResponseDto> submitAnswer(@RequestBody SurveyResultRequestDto surveyResultRequestDto) {

        SurveyResultResponseDto surveyResultResponseDto = surveyResultService.addResult(surveyResultRequestDto);

        return new ResponseEntity<>(surveyResultResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyResultListResponseDto> viewSurveyResults(@PathVariable(value = "id") String surveyId) {

        SurveyResultListResponseDto surveyResultListResponseDto = surveyResultService.viewSurveyResults(surveyId);

        return new ResponseEntity<>(surveyResultListResponseDto, HttpStatus.OK);
    }
}
