package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.*;
import com.hsenid.surveyapp.exceptions.NotFoundException;
import com.hsenid.surveyapp.model.Question;
import com.hsenid.surveyapp.model.Survey;
import com.hsenid.surveyapp.model.User;
import com.hsenid.surveyapp.repositoy.SurveyRepository;
import com.hsenid.surveyapp.repositoy.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {


    @Autowired
    SurveyRepository surveyRepository;
    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

    @Override
    public SurveyResponseDto createSurvey(final SurveyRequestDto surveyRequestDto) {
        logger.info("creating survey...");
        Survey survey = new Survey();
        List<Question> questions = new ArrayList<>();
        List<User> users = new ArrayList<>();
        if (Objects.nonNull(surveyRequestDto.getQuestions())) {
            surveyRequestDto.getQuestions().stream().forEach(questionDto -> {
                Question question = new Question();
                question.setId(questionDto.getId());
                question.setText(questionDto.getText());
                question.setType(questionDto.getType());
                question.setOptions(questionDto.getOptions());
                questions.add(question);
            });
        }
        survey.setTitle(surveyRequestDto.getTitle());
        survey.setDescription(surveyRequestDto.getDescription());
        survey.setQuestions(questions);
        survey.setAssignedTo(users);
        surveyRepository.save(survey);
        logger.info("survey details saved");
        return SurveyResponseDto.builder().id(survey.getId()).title(survey.getTitle()).description(survey.getDescription()).questions(convertToQuestionResponseDto(questions)).assigned(convertToUserResponseDto(users)).build();
    }

    @Override
    public SurveyResponseDto updateSurvey(final String surveyId, final SurveyRequestDto surveyRequestDto) {
        Optional<Survey> surveyObj = surveyRepository.findById(surveyId);
        logger.info("update survey details");
        if (surveyObj.isPresent()) {
            Survey survey = surveyObj.get();
            List<Question> questions = new ArrayList<>();
            List<User> users = new ArrayList<>();
            if (Objects.nonNull(surveyRequestDto.getQuestions())) {
                surveyRequestDto.getQuestions().forEach(questionDto -> {
                    Question question = new Question();
                    question.setId(questionDto.getId());
                    question.setText(questionDto.getText());
                    question.setType(questionDto.getType());
                    question.setOptions(questionDto.getOptions());
                    questions.add(question);
                });
            }
            if (Objects.nonNull(surveyRequestDto.getAssigned())) {
                surveyRequestDto.getAssigned().stream().forEach(userRequestDto -> {
                    User user = new User();
                    user.setId(userRequestDto.getId());
                    user.setUsername(userRequestDto.getUsername());
                    user.setEmail(userRequestDto.getEmail());
                    users.add(user);
                });
            }
            survey.setTitle(surveyRequestDto.getTitle());
            survey.setDescription(surveyRequestDto.getDescription());
            survey.setQuestions(questions);
            survey.setAssignedTo(users);
            surveyRepository.save(survey);
            logger.info("updated survey: " + survey.getId());
            return SurveyResponseDto.builder().id(survey.getId()).title(survey.getTitle()).description(survey.getDescription()).questions(convertToQuestionResponseDto(questions)).assigned(convertToUserResponseDto(users)).build();
        } else {
            logger.info("survey details not found");
            throw new NotFoundException("survey details not found");
        }
    }

    @Override
    public List<SurveyResponseDto> getAllSurveysWithQuestions() {
        logger.info("get surveys with questions");
        List<Survey> surveyList = surveyRepository.findAll();
        List<SurveyResponseDto> surveyResponseDtoList = new ArrayList<>();
        surveyList.forEach(survey -> {
            SurveyResponseDto surveyResponseDto = new SurveyResponseDto();
            surveyResponseDto.setId(survey.getId());
            surveyResponseDto.setTitle(survey.getTitle());
            surveyResponseDto.setDescription(survey.getDescription());
            try {
                surveyResponseDto.setQuestions(convertToQuestionResponseDto(survey.getQuestions()));
            } catch (Exception exception) {
                surveyResponseDto.setQuestions(null);
            }
            try {
                surveyResponseDto.setAssigned(convertToUserResponseDto(survey.getAssignedTo()));
            } catch (Exception exception) {
                surveyResponseDto.setAssigned(null);
            }
            surveyResponseDtoList.add(surveyResponseDto);
        });
        return surveyResponseDtoList;
    }

    @Override
    public List<SurveyResponseDto> getUserAssignedSurveys(final String userId) {
        logger.info("getting surveys assigned for user: " + userId);
        List<Survey> surveyList = surveyRepository.findAll();
        List<SurveyResponseDto> surveyResponseDtoList = new ArrayList<>();
        surveyList.forEach(survey -> {
            if (!survey.getAssignedTo().isEmpty()) {
            }
            List<User> users = survey.getAssignedTo();

            users.forEach(user -> {
                if (Objects.nonNull(user)) {
                    if (user.getId().equals(userId)) {
                        SurveyResponseDto surveyResponseDto = new SurveyResponseDto();
                        surveyResponseDto.setId(survey.getId());
                        surveyResponseDto.setTitle(survey.getTitle());
                        surveyResponseDto.setDescription(survey.getDescription());
                        surveyResponseDto.setQuestions(convertToQuestionResponseDto(survey.getQuestions()));
                        surveyResponseDto.setAssigned(convertToUserResponseDto(survey.getAssignedTo()));
                        surveyResponseDtoList.add(surveyResponseDto);
                        logger.info("total number of survey records: " + surveyResponseDtoList.size());
                    }
                }
            });
        });
        return surveyResponseDtoList;
    }

    @Override
    public SurveyResponseDto getSurveyById(final String surveyId) {
        logger.info("getting survey: " + surveyId);
        Optional<Survey> surveyObj = surveyRepository.findById(surveyId);
        if (surveyObj.isPresent()) {
            Survey survey = surveyObj.get();
            List<Question> questions = survey.getQuestions();

            List<User> users = survey.getAssignedTo();
            return SurveyResponseDto.builder().id(survey.getId()).title(survey.getTitle()).description(survey.getDescription()).questions(convertToQuestionResponseDto(questions)).assigned(convertToUserResponseDto(users)).build();
        } else {
            logger.info("survey details not found");
            throw new NotFoundException("survey details not found");
        }
    }

    @Override
    public void deleteSurveyById(final String surveyId) {
        logger.info("deleting survey: " + surveyId);
        Optional<Survey> surveyObj = surveyRepository.findById(surveyId);
        if (surveyObj.isPresent()) {
            surveyRepository.deleteById(surveyId);
        } else {
            logger.info("survey details not found");
            throw new NotFoundException("survey details not found");
        }
    }

    private List<QuestionResponseDto> convertToQuestionResponseDto(List<Question> questions) {
        logger.info("map list of questions to list of question response dto");
        List<QuestionResponseDto> questionResponseDtoList = new ArrayList<>();
        if (!questions.isEmpty()) {
            questions.forEach(question -> {
                if (Objects.nonNull(question)) {
                    QuestionResponseDto questionResponseDto = new QuestionResponseDto();
                    questionResponseDto.setId(question.getId());
                    questionResponseDto.setText(question.getText());
                    questionResponseDto.setType(question.getType());
                    questionResponseDto.setOptions(question.getOptions());
                    questionResponseDtoList.add(questionResponseDto);
                }
            });
        }
        return questionResponseDtoList;
    }

    private List<UserResponseDto> convertToUserResponseDto(List<User> users) {
        logger.info("map list of user to list of user response dto");
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        if (!users.isEmpty()) {
            users.forEach(user -> {
                if (Objects.nonNull(user)) {
                    UserResponseDto userResponseDto = new UserResponseDto();
                    userResponseDto.setId(user.getId());
                    userResponseDto.setUsername(user.getUsername());
                    userResponseDto.setEmail(user.getEmail());
                    userResponseDtoList.add(userResponseDto);
                }
            });
        }
        return userResponseDtoList;
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
