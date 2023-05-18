package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.*;
import com.hsenid.surveyapp.model.Question;
import com.hsenid.surveyapp.model.Survey;
import com.hsenid.surveyapp.model.SurveyResult;
import com.hsenid.surveyapp.model.User;
import com.hsenid.surveyapp.repositoy.QuestionRepository;
import com.hsenid.surveyapp.repositoy.SurveyRepository;
import com.hsenid.surveyapp.repositoy.SurveyResultRepository;
import com.hsenid.surveyapp.repositoy.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SurveyResultServiceImpl implements SurveyResultService {

    @Autowired
    private SurveyResultRepository surveyResultRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(SurveyResultServiceImpl.class);

    @Override
    public SurveyResultResponseDto addResult(final SurveyResultRequestDto surveyResultRequestDto) {
        logger.info("adding new result to survey : " + surveyResultRequestDto.getSurveyId());
        SurveyResult surveyResult = new SurveyResult();
        User user = new User();
        Survey survey = new Survey();
        user.setId(surveyResultRequestDto.getUserId());
        survey.setId(surveyResultRequestDto.getSurveyId());
        surveyResult.setUser(user);
        surveyResult.setSurvey(survey);
        surveyResult.setQuestionMap(surveyResultRequestDto.getQuestionMap());
        surveyResultRepository.save(surveyResult);
        logger.info("entered new survey result for " + surveyResultRequestDto.getSurveyId());
        return SurveyResultResponseDto.builder().surveyId(survey.getId()).userId(user.getId()).questionMap(surveyResultRequestDto.getQuestionMap()).numberOfAnswers(surveyResultRequestDto.getQuestionMap().size()).build();
    }

    @Override
    public SurveyResultListResponseDto viewSurveyResults(final String surveyId) {
        logger.info("loading summary results of " + surveyId);
        List<SurveyResultResponseDto> surveyResultResponseDtoList = new ArrayList<>();
        List<SurveyResult> surveyResultList = surveyResultRepository.findAll();
        List<String> questionIds = new ArrayList<>();
        surveyResultList.stream().filter(surveyResult -> {
            if (surveyResult.getSurvey().getId().equals(surveyId)) {
                Map<String, String> questionMap = new HashMap<>();
                surveyResult.getQuestionMap().forEach((questionID, answer) -> {
                    questionIds.add(questionID);
                    List<Question> questions = questionRepository.findAllById(questionIds);
                    questions.forEach(question -> {
                        if (question.getId().equals(questionID)) {
                            questionMap.put(question.getText(), answer);
                        }
                    });
                });
                SurveyResultResponseDto surveyResultResponseDto = new SurveyResultResponseDto();
                surveyResultResponseDto.setSurveyId(surveyResult.getSurvey().getId());
                surveyResultResponseDto.setUserId(surveyResult.getUser().getId());
                surveyResultResponseDto.setQuestionMap(questionMap);
                surveyResultResponseDto.setNumberOfAnswers(surveyResult.getQuestionMap().size());
                surveyResultResponseDtoList.add(surveyResultResponseDto);
                logger.info("loaded survey results of " + surveyId);
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        return SurveyResultListResponseDto.builder().surveyResultListResponseDtoList(surveyResultResponseDtoList).respondents(surveyResultResponseDtoList.size()).build();
    }

    @Override
    public List<SurveyResponseDto> viewUserCompletedSurveys(String userId) {
        List<SurveyResult> surveyResultList = surveyResultRepository.findAll();
        List<String> surveyIds = new ArrayList<>();
        surveyResultList.stream().filter(surveyResult -> {
            if (surveyResult.getUser().getId().equals(userId)) {
                surveyIds.add(surveyResult.getSurvey().getId());
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        List<Survey> surveyList = surveyRepository.findAllById(surveyIds);

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
            List<User> users = userRepository.findAllById(survey.getAssignedTo());
            List<String> assignedUsers = new ArrayList<>();
            users.forEach(user -> {
                assignedUsers.add(user.getUsername());
            });
            surveyResponseDto.setAssigned(assignedUsers);
            surveyResponseDtoList.add(surveyResponseDto);
        });
        return surveyResponseDtoList;
    }

    @Override
    public SurveyResultResponseDto viewUserSubmittedResult(final String surveyId, final String userId) {
        logger.info("loading summary results of " + surveyId);
        List<SurveyResultResponseDto> surveyResultResponseDtoList = new ArrayList<>();
        List<SurveyResult> surveyResultList = surveyResultRepository.findAll();
        List<String> questionIds = new ArrayList<>();
        surveyResultList.stream().filter(surveyResult -> {
            if (surveyResult.getSurvey().getId().equals(surveyId) && surveyResult.getUser().getId().equals(userId)) {
                Map<String, String> questionMap = new HashMap<>();
                surveyResult.getQuestionMap().forEach((questionID, answer) -> {
                    questionIds.add(questionID);
                    List<Question> questions = questionRepository.findAllById(questionIds);
                    questions.forEach(question -> {
                        if (question.getId().equals(questionID)) {
                            questionMap.put(question.getText(), answer);
                        }
                    });
                });
                SurveyResultResponseDto surveyResultResponseDto = new SurveyResultResponseDto();
                surveyResultResponseDto.setSurveyId(surveyResult.getSurvey().getId());
                surveyResultResponseDto.setUserId(surveyResult.getUser().getId());
                surveyResultResponseDto.setSurveyName(surveyResult.getSurvey().getTitle());
                surveyResultResponseDto.setSurveyDescription(surveyResult.getSurvey().getDescription());
                surveyResultResponseDto.setQuestionMap(questionMap);
                surveyResultResponseDto.setNumberOfAnswers(surveyResult.getQuestionMap().size());
                surveyResultResponseDtoList.add(surveyResultResponseDto);
                logger.info("loaded survey results of " + surveyId);
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        return surveyResultResponseDtoList.get(0);
    }

    private List<QuestionResponseDto> convertToQuestionResponseDto(List<Question> questions) {
        logger.info("map list of questions to list of question response dto");
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

    @Override
    public SurveyResultListResponseDto getAllSurveys() {

        List<SurveyResult> surveyResultList = surveyResultRepository.findAll();
        List<SurveyResultResponseDto> surveyResultResponseDtoList = new ArrayList<>();
        List<String> questionIds = new ArrayList<>();
        surveyResultList.forEach(surveyResult -> {
            Map<String, String> questionMap = new HashMap<>();
            surveyResult.getQuestionMap().forEach((questionID, answer) -> {
                questionIds.add(questionID);
                List<Question> questions = questionRepository.findAllById(questionIds);
                questions.forEach(question -> {
                    if (question.getId().equals(questionID)) {
                        questionMap.put(question.getText(), answer);
                    }
                });
            });
            SurveyResultResponseDto surveyResultResponseDto = new SurveyResultResponseDto();
            surveyResultResponseDto.setSurveyId(surveyResult.getSurvey().getId());
            surveyResultResponseDto.setSurveyName(surveyResult.getSurvey().getTitle());
            surveyResultResponseDto.setSurveyDescription(surveyResult.getSurvey().getDescription());
            surveyResultResponseDto.setUserId(surveyResult.getUser().getId());
            surveyResultResponseDto.setQuestionMap(questionMap);
            surveyResultResponseDto.setNumberOfAnswers(surveyResult.getQuestionMap().size());
            surveyResultResponseDtoList.add(surveyResultResponseDto);
            logger.info("loaded survey results of ");
        });
        return SurveyResultListResponseDto.builder().surveyResultListResponseDtoList(surveyResultResponseDtoList).respondents(surveyResultResponseDtoList.size()).build();
    }

    public QuestionAnswerResponseListDto getResponseData(SurveyResultListResponseDto surveyResultListResponseDto) {

        QuestionAnswerResponseDto questionAnswerResponseDto = new QuestionAnswerResponseDto();
        QuestionAnswerResponseListDto questionAnswerResponseListDto = new QuestionAnswerResponseListDto();

        surveyResultListResponseDto.getSurveyResultListResponseDtoList().forEach(surveyResultResponseDto -> {
            int count = 0;
            Optional<Survey> surveyObj = surveyRepository.findById(surveyResultResponseDto.getSurveyId());
            if (surveyObj.isPresent()) {
                Survey survey = surveyObj.get();

                survey.getQuestions().forEach(question -> {
                    List<AnswerDto> answerDtoList = new ArrayList<>();
                    AnswerDto answerDto = new AnswerDto();
                    question.getOptions().forEach(s -> {
                        answerDto.setOption(s);
                        answerDtoList.add(answerDto);
                    });
                    questionAnswerResponseDto.setQuestion(question.getText());
                    questionAnswerResponseDto.setAnswerDto(answerDtoList);
                });

            }
        });

        return questionAnswerResponseListDto;
    }
}
