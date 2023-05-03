package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.QuestionRequestDto;
import com.hsenid.surveyapp.dto.SurveyRequestDto;
import com.hsenid.surveyapp.dto.SurveyResponseDto;
import com.hsenid.surveyapp.dto.UserRequestDto;
import com.hsenid.surveyapp.exceptions.NotFoundException;
import com.hsenid.surveyapp.model.Question;
import com.hsenid.surveyapp.model.Survey;
import com.hsenid.surveyapp.model.User;
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
public class SurveyService {

    @Autowired
    SurveyRepository surveyRepository;

    Logger logger = LoggerFactory.getLogger(SurveyService.class);

    public SurveyResponseDto createSurvey(SurveyRequestDto surveyRequestDto) {

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

        return SurveyResponseDto.builder().title(survey.getTitle()).description(survey.getDescription()).questions(surveyRequestDto.getQuestions()).assigned(survey.getAssignedTo()).build();
    }

    public SurveyResponseDto updateSurvey(String surveyId, SurveyRequestDto surveyRequestDto) {

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

            return SurveyResponseDto.builder().title(survey.getTitle()).description(survey.getDescription()).questions(surveyRequestDto.getQuestions()).assigned(survey.getAssignedTo()).build();
        } else {
            logger.info("survey details not found");
            throw new NotFoundException("survey details not found");
        }
    }

    public List<SurveyResponseDto> getAllSurveysWithQuestions() {
        List<Survey> surveyList = surveyRepository.findAll();
        List<SurveyResponseDto> surveyResponseDtoList = new ArrayList<>();

        surveyList.forEach(survey -> {
            SurveyResponseDto surveyResponseDto = new SurveyResponseDto();
            surveyResponseDto.setTitle(survey.getTitle());
            surveyResponseDto.setDescription(survey.getDescription());
            try {
                surveyResponseDto.setQuestions(convertToQuestionDto(survey.getQuestions()));
            } catch (Exception exception) {
                surveyResponseDto.setQuestions(null);
            }
            surveyResponseDto.setAssigned(survey.getAssignedTo());
            surveyResponseDtoList.add(surveyResponseDto);

        });

        return surveyResponseDtoList;
    }

    public List<SurveyResponseDto> getUserAssignedSurveys(String userId) {
        List<Survey> surveyList = surveyRepository.findAll();
        List<SurveyResponseDto> surveyResponseDtoList = new ArrayList<>();


        surveyList.stream().filter(survey -> {
            List<String> userIds = survey.getAssignedTo();

            if (userIds.contains(userId)) {
                SurveyResponseDto surveyResponseDto = new SurveyResponseDto();
                logger.info(survey.getId());
                surveyResponseDto.setTitle(survey.getTitle());
                surveyResponseDto.setDescription(survey.getDescription());
                surveyResponseDto.setQuestions(convertToQuestionDto(survey.getQuestions()));
                surveyResponseDto.setAssigned(survey.getAssignedTo());
                surveyResponseDtoList.add(surveyResponseDto);
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        return surveyResponseDtoList;
    }


    private List<QuestionRequestDto> convertToQuestionDto(List<Question> questions) {

        List<QuestionRequestDto> questionRequestDtoList = new ArrayList<>();

        questions.forEach(question -> {
            QuestionRequestDto questionRequestDto = new QuestionRequestDto();
            questionRequestDto.setId(question.getId());
            questionRequestDto.setText(question.getText());
            questionRequestDto.setType(question.getType());
            questionRequestDto.setOptions(question.getOptions());

            questionRequestDtoList.add(questionRequestDto);
        });

        return questionRequestDtoList;
    }

    public String deleteSurveyById(String surveyId) {
        Optional<Survey> surveyObj = surveyRepository.findById(surveyId);
        if (surveyObj.isPresent()) {
            surveyRepository.deleteById(surveyId);
            return surveyId;
        } else {
            logger.info("survey details not found");
            throw new NotFoundException("survey details not found");
        }

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
