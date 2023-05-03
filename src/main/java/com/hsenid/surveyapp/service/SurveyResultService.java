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
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SurveyResultService {

    @Autowired
    private SurveyResultRepository surveyResultRepository;
    @Autowired
    private QuestionRepository questionRepository;

    Logger logger = LoggerFactory.getLogger(SurveyService.class);

    public SurveyResultResponseDto addResult(SurveyResultRequestDto surveyResultRequestDto) {

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

    public SurveyResultListResponseDto viewSurveyResults(String surveyId) {

        List<SurveyResultResponseDto> surveyResultResponseDtoList = new ArrayList<>();

        List<SurveyResult> surveyResultList = surveyResultRepository.findAll();
        List<String> questionIds = new ArrayList<>();

        surveyResultList.stream().filter(surveyResult -> {
            if (surveyResult.getSurvey().getId().equals(surveyId)) {
                SurveyResultResponseDto surveyResultResponseDto = new SurveyResultResponseDto();

                surveyResultResponseDto.setSurveyId(surveyResult.getSurvey().getId());
                surveyResultResponseDto.setUserId(surveyResult.getUser().getId());
                //surveyResultResponseDto.setQuestionMap(surveyResult.getQuestionMap());

                Map<String, String> questions = surveyResult.getQuestionMap();
                Map<String, String> newQuestions = surveyResult.getQuestionMap();
                logger.info("CHECK: "+questions);

                try {
                    questions.forEach((question, answer) -> {
                        questionIds.add(question);
                        List<Question> surveyQuestions = questionRepository.findAllById(questionIds);
                        logger.info("FROM DB: " + surveyQuestions.size());
                        surveyQuestions.stream().forEach(question1 -> {
                            newQuestions.put(question1.getText(), answer);
                        });
                    });
                }catch (Exception exception){

                }
                surveyResultResponseDto.setQuestionMap(newQuestions);

                surveyResultResponseDto.setNumberOfAnswers(surveyResult.getQuestionMap().size());
                surveyResultResponseDtoList.add(surveyResultResponseDto);
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        return SurveyResultListResponseDto.builder().surveyResultListResponseDtoList(surveyResultResponseDtoList).respondents(surveyResultResponseDtoList.size()).build();
    }
}
