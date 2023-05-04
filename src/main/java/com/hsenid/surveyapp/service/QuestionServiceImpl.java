package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.QuestionRequestDto;
import com.hsenid.surveyapp.dto.QuestionResponseDto;
import com.hsenid.surveyapp.exceptions.NotFoundException;
import com.hsenid.surveyapp.model.Question;
import com.hsenid.surveyapp.repositoy.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

    @Override
    public QuestionResponseDto createQuestion(final QuestionRequestDto questionRequestDto) {

        Question question = new Question();
        question.setText(questionRequestDto.getText());
        question.setType(questionRequestDto.getType());
        question.setOptions(questionRequestDto.getOptions());

        Question ques = questionRepository.save(question);

        return QuestionResponseDto.builder().id(ques.getId()).text(question.getText()).type(question.getType()).options(question.getOptions()).build();
    }

    @Override
    public QuestionResponseDto updateQuestion(final QuestionRequestDto questionRequestDto, final String id) {

        Optional<Question> questionObj = questionRepository.findById(id);
        if (questionObj.isPresent()) {
            Question question = questionObj.get();
            question.setText(questionRequestDto.getText());
            question.setType(questionRequestDto.getType());
            question.setOptions(questionRequestDto.getOptions());

            Question ques = questionRepository.save(question);

            return QuestionResponseDto.builder().id(ques.getId()).text(question.getText()).type(question.getType()).options(question.getOptions()).build();
        }
        return null;
    }

    @Override
    public void deleteQuestion(final String questionId) {

        Optional<Question> questionObj = questionRepository.findById(questionId);

        if (questionObj.isPresent()) {
            Question question = questionObj.get();
            questionRepository.deleteById(questionId);
        }else {
            logger.info("question not found");
            throw new NotFoundException("question not found");
        }
    }

    @Override
    public QuestionResponseDto getQuestionById(final String questionId) {

        Optional<Question> questionObj = questionRepository.findById(questionId);

        if (questionObj.isPresent()) {
            Question question = questionObj.get();
            return QuestionResponseDto.builder().id(question.getId()).text(question.getText()).type(question.getType()).options(question.getOptions()).build();
        }else {
            logger.info("question not found");
            throw new NotFoundException("question not found");
        }
    }

    @Override
    public List<QuestionResponseDto> getQuestions() {
        List<QuestionResponseDto> questionResponseDtoList = new ArrayList<>();
        List<Question> questionResponse = questionRepository.findAll();
        questionResponse.forEach(question -> {
            questionResponseDtoList.add(QuestionResponseDto.builder().text(question.getText()).type(question.getType()).options(question.getOptions()).build());
        });
        return questionResponseDtoList;
    }

    @Override
    public List<QuestionResponseDto> getQuestionsByType(final String type) {
        List<QuestionResponseDto> questionResponseDtos = new ArrayList<>();
        List<Question> questionResponse = questionRepository.findAll();
        questionResponse.forEach(question -> {
            if (question.getType().equals(type))
                questionResponseDtos.add(QuestionResponseDto.builder().text(question.getText()).type(question.getType()).options(question.getOptions()).build());
        });
        return questionResponseDtos;
    }
}
