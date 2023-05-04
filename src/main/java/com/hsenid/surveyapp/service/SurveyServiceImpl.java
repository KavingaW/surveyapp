package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.*;
import com.hsenid.surveyapp.exceptions.NotFoundException;
import com.hsenid.surveyapp.model.Question;
import com.hsenid.surveyapp.model.Survey;
import com.hsenid.surveyapp.repositoy.SurveyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    SurveyRepository surveyRepository;

    Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

    @Override
    public SurveyResponseDto createSurvey(final SurveyRequestDto surveyRequestDto) {

        Survey survey = new Survey();
        List<Question> questions = new ArrayList<>();
        List<String> users = new ArrayList<>();

        surveyRequestDto.getQuestions().stream().forEach(questionDto -> {
            Question question = new Question();
            question.setId(questionDto.getId());
            question.setText(questionDto.getText());
            question.setType(questionDto.getType());
            question.setOptions(questionDto.getOptions());
            questions.add(question);

        });

        surveyRequestDto.getAssigned().stream().forEach(userRequestDto -> {
            users.add(userRequestDto.getId());
        });

        survey.setTitle(surveyRequestDto.getTitle());
        survey.setDescription(surveyRequestDto.getDescription());
        survey.setQuestions(questions);
        survey.setAssignedTo(users);

        surveyRepository.save(survey);

        logger.info("survey details saved");

        return SurveyResponseDto.builder().title(survey.getTitle()).description(survey.getDescription()).questions(convertToQuestionResponseDto(questions)).assigned(survey.getAssignedTo()).build();
    }

    @Override
    public SurveyResponseDto updateSurvey(final String surveyId, final SurveyRequestDto surveyRequestDto) {

        Optional<Survey> surveyObj = surveyRepository.findById(surveyId);

        logger.info("update survey details");
        if (surveyObj.isPresent()) {
            Survey survey = surveyObj.get();

            List<Question> questions = new ArrayList<>();
            List<String> users = new ArrayList<>();

            surveyRequestDto.getQuestions().forEach(questionDto -> {
                Question question = new Question();
                question.setId(questionDto.getId());
                question.setText(questionDto.getText());
                question.setType(questionDto.getType());
                question.setOptions(questionDto.getOptions());
                questions.add(question);
            });

            surveyRequestDto.getAssigned().stream().forEach(userRequestDto -> {
                users.add(userRequestDto.getId());
            });

            survey.setTitle(surveyRequestDto.getTitle());
            survey.setDescription(surveyRequestDto.getDescription());
            survey.setQuestions(questions);
            survey.setAssignedTo(users);

            surveyRepository.save(survey);
            logger.info("updated survey: " + survey.getId());

            return SurveyResponseDto.builder().title(survey.getTitle()).description(survey.getDescription()).questions(convertToQuestionResponseDto(questions)).assigned(survey.getAssignedTo()).build();
        } else {
            logger.info("survey details not found");
            throw new NotFoundException("survey details not found");
        }
    }

    @Override
    public List<SurveyResponseDto> getAllSurveysWithQuestions() {
        List<Survey> surveyList = surveyRepository.findAll();
        List<SurveyResponseDto> surveyResponseDtoList = new ArrayList<>();

        surveyList.forEach(survey -> {
            SurveyResponseDto surveyResponseDto = new SurveyResponseDto();
            surveyResponseDto.setTitle(survey.getTitle());
            surveyResponseDto.setDescription(survey.getDescription());
            try {
                surveyResponseDto.setQuestions(convertToQuestionResponseDto(survey.getQuestions()));
            } catch (Exception exception) {
                surveyResponseDto.setQuestions(null);
            }
            surveyResponseDto.setAssigned(survey.getAssignedTo());
            surveyResponseDtoList.add(surveyResponseDto);

        });

        return surveyResponseDtoList;
    }

    @Override
    public List<SurveyResponseDto> getUserAssignedSurveys(final String userId) {
        List<Survey> surveyList = surveyRepository.findAll();
        List<SurveyResponseDto> surveyResponseDtoList = new ArrayList<>();


        surveyList.stream().filter(survey -> {
            List<String> userIds = survey.getAssignedTo();

            if (userIds.contains(userId)) {
                SurveyResponseDto surveyResponseDto = new SurveyResponseDto();
                surveyResponseDto.setTitle(survey.getTitle());
                surveyResponseDto.setDescription(survey.getDescription());
                surveyResponseDto.setQuestions(convertToQuestionResponseDto(survey.getQuestions()));
                surveyResponseDto.setAssigned(survey.getAssignedTo());
                surveyResponseDtoList.add(surveyResponseDto);
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        return surveyResponseDtoList;
    }

    @Override
    public SurveyResponseDto getSurveyById(final String surveyId) {
        Optional<Survey> surveyObj = surveyRepository.findById(surveyId);
        if (surveyObj.isPresent()) {
            Survey survey = surveyObj.get();
            List<Question> questions = survey.getQuestions();

            return SurveyResponseDto.builder().title(survey.getTitle()).description(survey.getDescription()).questions(convertToQuestionResponseDto(questions)).assigned(survey.getAssignedTo()).build();
        } else {
            logger.info("survey details not found");
            throw new NotFoundException("survey details not found");
        }
    }

    @Override
    public void deleteSurveyById(final String surveyId) {
        Optional<Survey> surveyObj = surveyRepository.findById(surveyId);
        if (surveyObj.isPresent()) {
            surveyRepository.deleteById(surveyId);
        } else {
            logger.info("survey details not found");
            throw new NotFoundException("survey details not found");
        }

    }

    private List<QuestionResponseDto> convertToQuestionResponseDto(List<Question> questions) {

        List<QuestionResponseDto> questionResponseDtoList = new ArrayList<>();

        questions.forEach(question -> {
            QuestionResponseDto questionResponseDto = new QuestionResponseDto();
            questionResponseDto.setId(question.getId());
            questionResponseDto.setText(question.getText());
            questionResponseDto.setType(question.getType());
            questionResponseDto.setOptions(question.getOptions());

            questionResponseDtoList.add(questionResponseDto);
        });

        return questionResponseDtoList;
    }

    private Question convertQuestionResponseDto(QuestionRequestDto questionRequestDto) {

        Question question = new Question();
        question.setId(questionRequestDto.getId());
        question.setText(questionRequestDto.getText());
        question.setType(questionRequestDto.getType());
        question.setOptions(questionRequestDto.getOptions());

        return question;
    }
}
