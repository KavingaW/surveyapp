package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.QuestionRequestDto;
import com.hsenid.surveyapp.dto.QuestionResponseDto;
import com.hsenid.surveyapp.model.Question;
import com.hsenid.surveyapp.repositoy.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    public QuestionResponseDto createQuestion(QuestionRequestDto questionRequestDto) {

        Question question = new Question();

        question.setText(questionRequestDto.getText());
        question.setType(questionRequestDto.getType());
        question.setOptions(questionRequestDto.getOptions());

        questionRepository.save(question);
        return QuestionResponseDto.builder().text(question.getText()).type(question.getType()).options(question.getOptions()).build();
    }

    public QuestionResponseDto updateQuestion(QuestionRequestDto questionRequestDto, String id) {
        Optional<Question> questionObj = questionRepository.findById(id);
        if (questionObj.isPresent()) {
            Question question = questionObj.get();

            question.setText(questionRequestDto.getText());
            question.setType(questionRequestDto.getType());
            question.setOptions(questionRequestDto.getOptions());

            questionRepository.save(question);

            return QuestionResponseDto.builder().text(question.getText()).options(question.getOptions()).build();
        }
        return null;
    }

    public QuestionResponseDto deleteQuestion(String questionId) {
        Optional<Question> questionObj = questionRepository.findById(questionId);
        if (questionObj.isPresent()) {
            Question question = questionObj.get();
            questionRepository.deleteById(questionId);
        }
        return null;
    }

    public QuestionResponseDto getQuestionById(String questionId) {
        Optional<Question> questionObj = questionRepository.findById(questionId);
        if (questionObj.isPresent()) {
            Question question = questionObj.get();
            return QuestionResponseDto.builder().text(question.getText()).type(question.getType()).options(question.getOptions()).build();
        }
        return null;
    }

    public List<QuestionResponseDto> getQuestions() {

        List<QuestionResponseDto> questionResponseDtos = new ArrayList<>();
        List<Question> questionResponse = questionRepository.findAll();

        questionResponse.forEach(question -> {
            questionResponseDtos.add(QuestionResponseDto.builder().text(question.getText()).type(question.getType()).options(question.getOptions()).build());
        });

        return questionResponseDtos;

    }

    public List<QuestionResponseDto> getQuestionsByType(String type){
        List<QuestionResponseDto> questionResponseDtos = new ArrayList<>();
        List<Question> questionResponse = questionRepository.findAll();

        questionResponse.forEach(question -> {
            if (question.getType().equals(type))
            questionResponseDtos.add(QuestionResponseDto.builder().text(question.getText()).type(question.getType()).options(question.getOptions()).build());
        });

        return questionResponseDtos;
    }
}
