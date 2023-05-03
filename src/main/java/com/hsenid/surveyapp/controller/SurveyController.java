package com.hsenid.surveyapp.controller;

import com.hsenid.surveyapp.dto.SurveyListResponseDto;
import com.hsenid.surveyapp.dto.SurveyRequestDto;
import com.hsenid.surveyapp.dto.SurveyResponseDto;
import com.hsenid.surveyapp.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    @Autowired
    SurveyService surveyService;

    @GetMapping("/home")
    public ResponseEntity<String> getHome() {
        return ResponseEntity.ok("This is home page");
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("This is dashboard page");
    }

    @GetMapping("/manage")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getManage() {
        return ResponseEntity.ok("This is manage page");
    }

    @PostMapping("/add")
    public ResponseEntity<SurveyResponseDto> addSurvey(@RequestBody SurveyRequestDto surveyRequestDto) {
        SurveyResponseDto surveyResponseDto = surveyService.createSurvey(surveyRequestDto);
        return new ResponseEntity<>(surveyResponseDto, HttpStatus.OK);

    }

    @GetMapping("/list")
    public ResponseEntity<SurveyListResponseDto> getSurveysWithQuestions(){
        List<SurveyResponseDto> surveyResponseDtoList = surveyService.getAllSurveysWithQuestions();

        return new ResponseEntity(surveyResponseDtoList,HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<SurveyListResponseDto> getUserAssignedSurveys(@PathVariable(value = "id") String userId){
        List<SurveyResponseDto> surveyResponseDtoList = surveyService.getUserAssignedSurveys(userId);

        return new ResponseEntity(surveyResponseDtoList,HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<SurveyResponseDto> updateSurvey(@PathVariable(value = "id") String id, @RequestBody SurveyRequestDto surveyRequestDto) {
        SurveyResponseDto surveyResponseDto = surveyService.updateSurvey(id, surveyRequestDto);
        return new ResponseEntity<>(surveyResponseDto,HttpStatus.OK);
    }

}