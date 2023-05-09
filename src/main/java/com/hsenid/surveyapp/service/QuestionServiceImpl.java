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

    Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Override
    public QuestionResponseDto createQuestion(final QuestionRequestDto questionRequestDto) {
        logger.info("creating new question...");
        Question question = new Question();
        question.setText(questionRequestDto.getText());
        question.setType(questionRequestDto.getType());
        question.setOptions(questionRequestDto.getOptions());
        Question ques = questionRepository.save(question);
        logger.info("created new question");
        return QuestionResponseDto.builder().id(ques.getId()).text(question.getText()).type(question.getType()).options(question.getOptions()).build();
    }

    @Override
    public QuestionResponseDto updateQuestion(final QuestionRequestDto questionRequestDto, final String questionId) {
        logger.info("updating question " + questionId);
        Optional<Question> questionObj = questionRepository.findById(questionId);
        if (questionObj.isPresent()) {
            Question question = questionObj.get();
            question.setText(questionRequestDto.getText());
            question.setType(questionRequestDto.getType());
            question.setOptions(questionRequestDto.getOptions());
            Question ques = questionRepository.save(question);
            logger.info("updated question " + questionId);
            return QuestionResponseDto.builder().id(ques.getId()).text(question.getText()).type(question.getType()).options(question.getOptions()).build();
        } else {
            logger.info("question not found");
            throw new NotFoundException("question not found");
        }
    }

    @Override
    public void deleteQuestion(final String questionId) {
        logger.info("deleting question " + questionId);
        Optional<Question> questionObj = questionRepository.findById(questionId);
        if (questionObj.isPresent()) {
            Question question = questionObj.get();
            questionRepository.deleteById(questionId);
            logger.info("deleted question " + questionId);
        } else {
            logger.info("question not found");
            throw new NotFoundException("question not found");
        }
    }

    @Override
    public QuestionResponseDto getQuestionById(final String questionId) {
        logger.info("getting question " + questionId);
        Optional<Question> questionObj = questionRepository.findById(questionId);
        if (questionObj.isPresent()) {
            Question question = questionObj.get();
            logger.info("retrieved question " + questionId);
            return QuestionResponseDto.builder().id(question.getId()).text(question.getText()).type(question.getType()).options(question.getOptions()).build();
        } else {
            logger.info("question not found");
            throw new NotFoundException("question not found");
        }
    }

    @Override
    public List<QuestionResponseDto> getQuestions() {
        logger.info("getting all questions");
        List<QuestionResponseDto> questionResponseDtoList = new ArrayList<>();
        List<Question> questionResponse = questionRepository.findAll();
        logger.info("retrieved total records " + questionResponse.size());
        try {
            questionResponse.forEach(question -> {
                questionResponseDtoList.add(QuestionResponseDto.builder().id(question.getId()).text(question.getText()).type(question.getType()).options(question.getOptions()).build());
            });
            return questionResponseDtoList;
        } catch (Exception exception) {
            logger.info("question not found");
            throw new NotFoundException("question not found");
        }
    }

    @Override
    public List<QuestionResponseDto> getQuestionsByType(final String type) {
        logger.info("getting questions of type: " + type);
        List<QuestionResponseDto> questionResponseDtos = new ArrayList<>();
        List<Question> questionResponse = questionRepository.findAll();
        try {
            questionResponse.forEach(question -> {
                if (question.getType().equals(type))
                    questionResponseDtos.add(QuestionResponseDto.builder().id(question.getId()).text(question.getText()).type(question.getType()).options(question.getOptions()).build());
            });
            logger.info("retrieved total records " + questionResponse.size());
            return questionResponseDtos;
        } catch (Exception exception) {
            logger.info("question not found with type: " + type);
            throw new NotFoundException("question not found");
        }

    }
}
