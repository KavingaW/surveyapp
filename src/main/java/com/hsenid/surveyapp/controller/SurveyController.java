package com.hsenid.surveyapp.controller;

import com.hsenid.surveyapp.dto.*;
import com.hsenid.surveyapp.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//test concurrency223899
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

    /**
     * add survey
     *
     * @param surveyRequestDto given SurveyRequestDto object
     * @return a {@link SurveyResponseDto} object
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SurveyResponseDto> addSurvey(@RequestBody final SurveyRequestDto surveyRequestDto) {
        SurveyResponseDto surveyResponseDto = surveyService.createSurvey(surveyRequestDto);
        return new ResponseEntity<>(surveyResponseDto, HttpStatus.OK);
    }

    /**
     * get survey list
     *
     * @return a {@link SurveyListResponseDto} object
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SurveyListResponseDto> getSurveysWithQuestions() {
        List<SurveyResponseDto> surveyResponseDtoList = surveyService.getAllSurveysWithQuestions();
        return new ResponseEntity(surveyResponseDtoList, HttpStatus.OK);
    }

    /**
     * get survey belonging to a user
     *
     * @param userId given user id
     * @return a {@link SurveyListResponseDto} object
     */
    @GetMapping("/list/{user-id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SurveyListResponseDto> getUserAssignedSurveys(@PathVariable(value = "user-id") final String userId) {
        List<SurveyResponseDto> surveyResponseDtoList = surveyService.getUserAssignedSurveys(userId);
        return new ResponseEntity(surveyResponseDtoList, HttpStatus.OK);
    }

    /**
     * update survey
     *
     * @param surveyRequestDto given survey object
     * @return a {@link SurveyResponseDto} object
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SurveyResponseDto> updateSurvey(@PathVariable(value = "id") final String surveyId, @RequestBody final SurveyRequestDto surveyRequestDto) {
        SurveyResponseDto surveyResponseDto = surveyService.updateSurvey(surveyId, surveyRequestDto);
        return new ResponseEntity<>(surveyResponseDto, HttpStatus.OK);
    }

    /**
     * get survey
     *
     * @param surveyId given survey id
     * @return a {@link SurveyResponseDto} object
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SurveyResponseDto> getSurveyById(@PathVariable(value = "id") final String surveyId) {
        SurveyResponseDto surveyResponseDto = surveyService.getSurveyById(surveyId);
        return new ResponseEntity<>(surveyResponseDto, HttpStatus.OK);
    }

    /**
     * delete survey
     *
     * @param surveyId given survey id
     * @return a {@link DeleteResponseDto} object
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeleteResponseDto> deleteSurveyById(@PathVariable(value = "id") final String surveyId) {
        surveyService.deleteSurveyById(surveyId);
        DeleteResponseDto deleteResponseDto = DeleteResponseDto.builder().code("200").message("DELETED").build();
        return new ResponseEntity<>(deleteResponseDto, HttpStatus.OK);
    }
}