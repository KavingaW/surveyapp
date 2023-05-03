package com.hsenid.surveyapp.controller;

import com.hsenid.surveyapp.dto.QuestionListResponseDto;
import com.hsenid.surveyapp.dto.QuestionRequestDto;
import com.hsenid.surveyapp.dto.QuestionResponseDto;
import com.hsenid.surveyapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PostMapping("/add")
    public ResponseEntity<QuestionResponseDto> addQuestion(@RequestBody QuestionRequestDto questionRequestDto) {
        QuestionResponseDto questionResponseDto = questionService.createQuestion(questionRequestDto);
        return new ResponseEntity<>(questionResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(@PathVariable(value = "id") final String questionId, @RequestBody final QuestionRequestDto questionRequestDto) {
        QuestionResponseDto questionResponseDto = questionService.updateQuestion(questionRequestDto, questionId);
        return new ResponseEntity<>(questionResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteQuestion(@PathVariable(value = "id") final String questionId) {
        QuestionResponseDto questionResponseDto = null;
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/")
    public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable(value = "id") final String questionId) {
        QuestionResponseDto questionResponseDto = questionService.getQuestionById(questionId);
        return new ResponseEntity(questionResponseDto, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<QuestionListResponseDto> getQuestions() {
        List<QuestionResponseDto> questionResponseDto = questionService.getQuestions();
        return new ResponseEntity(questionResponseDto, HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<QuestionListResponseDto> getQuestionsByType(@PathVariable(value = "type") final String type) {
        List<QuestionResponseDto> questionResponseDto = questionService.getQuestionsByType(type);
        return new ResponseEntity(questionResponseDto, HttpStatus.OK);
    }

}
