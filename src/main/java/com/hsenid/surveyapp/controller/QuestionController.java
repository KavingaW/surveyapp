package com.hsenid.surveyapp.controller;

import com.hsenid.surveyapp.dto.DeleteResponseDto;
import com.hsenid.surveyapp.dto.QuestionListResponseDto;
import com.hsenid.surveyapp.dto.QuestionRequestDto;
import com.hsenid.surveyapp.dto.QuestionResponseDto;
import com.hsenid.surveyapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    /**
     * add question
     *
     * @param questionRequestDto given QuestionRequestDto object
     * @return a {@link QuestionResponseDto} object
     */
    @PostMapping("/{survey-id}")
    public ResponseEntity<QuestionResponseDto> addQuestion(@PathVariable(value = "survey-id") final String surveyId, @RequestBody final QuestionRequestDto questionRequestDto) {
        QuestionResponseDto questionResponseDto = questionService.createQuestion(surveyId,questionRequestDto);
        return new ResponseEntity<>(questionResponseDto, HttpStatus.CREATED);
    }

    /**
     * update question
     *
     * @param questionRequestDto given QuestionRequestDto object
     * @return a {@link QuestionResponseDto} object
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(@PathVariable(value = "id") final String questionId, @RequestBody final QuestionRequestDto questionRequestDto) {
        QuestionResponseDto questionResponseDto = questionService.updateQuestion(questionRequestDto, questionId);
        return new ResponseEntity<>(questionResponseDto, HttpStatus.OK);
    }

    /**
     * delete question
     *
     * @param questionId given question id
     * @return a {@link QuestionResponseDto} object
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponseDto> deleteQuestion(@PathVariable(value = "id") final String questionId) {
        questionService.deleteQuestion(questionId);
        DeleteResponseDto responseDto = DeleteResponseDto.builder().code("200").message("Deleted").build();
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    /**
     * get question
     *
     * @param questionId given question id
     * @return a {@link QuestionResponseDto} object
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable(value = "id") final String questionId) {
        QuestionResponseDto questionResponseDto = questionService.getQuestionById(questionId);
        return new ResponseEntity(questionResponseDto, HttpStatus.OK);
    }

    /**
     * get question list
     *
     * @return a {@link QuestionListResponseDto} object
     */
    @GetMapping("/list")
    public ResponseEntity<QuestionListResponseDto> getQuestions() {
        List<QuestionResponseDto> questionResponseDto = questionService.getQuestions();
        return new ResponseEntity(questionResponseDto, HttpStatus.OK);
    }

    /**
     * get question by type
     *
     * @param type given question type
     * @return a {@link QuestionResponseDto} object
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<QuestionListResponseDto> getQuestionsByType(@PathVariable(value = "type") final String type) {
        List<QuestionResponseDto> questionResponseDto = questionService.getQuestionsByType(type);
        return new ResponseEntity(questionResponseDto, HttpStatus.OK);
    }

}
