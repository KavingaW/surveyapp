package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.SurveyResultListResponseDto;
import com.hsenid.surveyapp.dto.SurveyResultRequestDto;
import com.hsenid.surveyapp.dto.SurveyResultResponseDto;
import com.hsenid.surveyapp.model.Question;
import com.hsenid.surveyapp.model.Survey;
import com.hsenid.surveyapp.model.SurveyResult;
import com.hsenid.surveyapp.model.User;
import com.hsenid.surveyapp.repositoy.QuestionRepository;
import com.hsenid.surveyapp.repositoy.SurveyResultRepository;
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
//        Map<String, String> questions = surveyResult.getQuestionMap();
//        Map<String, String> newQuestions = surveyResult.getQuestionMap();
//        logger.info("CHECK: "+questions);
//
//        surveyResultResponseDto.setQuestionMap(newQuestions);
//
//        try {
//            questions.forEach((question, answer) -> {
//                questionIds.add(question);
//                List<Question> surveyQuestions = questionRepository.findAllById(questionIds);
//                logger.info("FROM DB: " + surveyQuestions.size());
//                surveyQuestions.stream().forEach(question1 -> {
//                    newQuestions.put(question1.getText(), answer);
//                });
//            });
//        }catch (Exception exception){
//
//        }
        return SurveyResultListResponseDto.builder().surveyResultListResponseDtoList(surveyResultResponseDtoList).respondents(surveyResultResponseDtoList.size()).build();
    }
}
