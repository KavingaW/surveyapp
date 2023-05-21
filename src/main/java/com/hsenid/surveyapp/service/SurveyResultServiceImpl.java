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
            if (Objects.nonNull(surveyResult.getUser())) {
                if (surveyResult.getUser().getId().equals(userId) && Objects.nonNull(surveyResult.getSurvey())) {
                    surveyIds.add(surveyResult.getSurvey().getId());
                    return true;
                } else {
                    return false;
                }
            }
            return false;
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
    public SurveyResultResponseDto viewUserSubmittedResult(final String surveyId, final String userId) {
        logger.info("loading summary results of " + surveyId);
        List<SurveyResultResponseDto> surveyResultResponseDtoList = new ArrayList<>();
        List<SurveyResult> surveyResultList = surveyResultRepository.findAll();
        List<String> questionIds = new ArrayList<>();
        surveyResultList.stream().filter(surveyResult -> {
            if (Objects.nonNull(surveyResult.getUser())) {
                if (Objects.nonNull(surveyResult.getSurvey())) {
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
                }

            }

            return false;
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

    @Override
    public List<SurveyResponseDto> getAllSurveys() {

        List<SurveyResult> surveyResultList = surveyResultRepository.findAll();
//        List<SurveyResultResponseDto> surveyResultResponseDtoList = new ArrayList<>();
//        List<String> questionIds = new ArrayList<>();
//        surveyResultList.forEach(surveyResult -> {
//            Map<String, String> questionMap = new HashMap<>();
//            surveyResult.getQuestionMap().forEach((questionID, answer) -> {
//                questionIds.add(questionID);
//                List<Question> questions = questionRepository.findAllById(questionIds);
//                questions.forEach(question -> {
//                    if (question.getId().equals(questionID)) {
//                        questionMap.put(question.getText(), answer);
//                    }
//                });
//            });
//            SurveyResultResponseDto surveyResultResponseDto = new SurveyResultResponseDto();
//            surveyResultResponseDto.setSurveyId(surveyResult.getSurvey().getId());
//            surveyResultResponseDto.setSurveyName(surveyResult.getSurvey().getTitle());
//            surveyResultResponseDto.setSurveyDescription(surveyResult.getSurvey().getDescription());
//            surveyResultResponseDto.setUserId(surveyResult.getUser().getId());
//            surveyResultResponseDto.setQuestionMap(questionMap);
//            surveyResultResponseDto.setNumberOfAnswers(surveyResult.getQuestionMap().size());
//            surveyResultResponseDtoList.add(surveyResultResponseDto);
//            logger.info("loaded survey results of ");
//        });

        List<SurveyResponseDto> surveyResponseDtoList = new ArrayList<>();
        List<String> surveyList = new ArrayList<>();
        surveyResultList.stream().forEach(surveyResult -> {
            Survey survey = surveyResult.getSurvey();
            if (Objects.nonNull(survey)) {
                surveyList.add(survey.getId());
            }

        });
        List<Survey> surveys = surveyRepository.findAllById(surveyList);
        if (!surveys.isEmpty()) {
            surveys.forEach(survey -> {
                SurveyResponseDto surveyResponseDto = new SurveyResponseDto();
                surveyResponseDto.setId(survey.getId());
                surveyResponseDto.setTitle(survey.getTitle());
                surveyResponseDto.setDescription(survey.getDescription());
                surveyResponseDtoList.add(surveyResponseDto);
            });

        }
        return surveyResponseDtoList;
    }

//    public SurveyAnswerResponseDto getResponseData(String surveyId) {
//
//        //Try01
////        SurveyAnswerResponseDto surveyAnswerResponseDto = new SurveyAnswerResponseDto();
////        List<SurveyResult> surveyResultList = surveyResultRepository.findAll();
////        List<Survey> surveyList = new ArrayList<>();
////        List<String> surveyResultIdList = new ArrayList<>();
////        List<String> questionIds = new ArrayList<>();
////        if (!surveyResultList.isEmpty()) {
////            surveyResultList.stream().filter(surveyResult -> {
////                if (surveyResult.getSurvey().getId().equals(surveyId)) {
////                    Map<String,String> questionMap = surveyResult.getQuestionMap();
////
////                    surveyResultIdList.add(surveyResult.getId());
////                    surveyList.add(surveyResult.getSurvey());
////                    return true;
////                }
////                return false;
////            }).collect(Collectors.toList());
////        }
////
////        System.out.println("SUREVEYRESULT ID"+surveyResultIdList);
////        surveyList.forEach(survey -> {
////            List<Question> questionList = survey.getQuestions();
////            List<QuestionAnswerResponseDto> questionAnswerResponseDtoList = new ArrayList<>();
////            questionList.forEach(question -> {
////                Integer answerResponses = 0;
////                QuestionAnswerResponseDto questionAnswerResponseDto = new QuestionAnswerResponseDto();
////                Map<String, Integer> responseAnswerMap = new HashMap<>();
////                List<String> answers = question.getOptions();
////                questionAnswerResponseDto.setId(question.getId());
////                questionAnswerResponseDto.setText(question.getText());
////
////
////
////                answers.forEach(s -> {
////                    responseAnswerMap.put(s, answerResponses);
////                });
////
////
////
////
////                questionAnswerResponseDto.setResponseAnswerMap(responseAnswerMap);
////                questionAnswerResponseDtoList.add(questionAnswerResponseDto);
////
////            });
////
////            surveyAnswerResponseDto.setId(survey.getId());
////            surveyAnswerResponseDto.setSurveyName(survey.getTitle());
////            surveyAnswerResponseDto.setQuestionAnswerResponseDtoList(questionAnswerResponseDtoList);
////            surveyAnswerResponseDto.setTotResponses(surveyList.size());
////
////        });
////
////
////        List<SurveyResult> surveyResultList1 =  surveyResultRepository.findAllById(surveyResultIdList);
////
////       // List<String> questionIds = new ArrayList<>();
////        Map<String, Integer> responseAnswerMap = new HashMap<>();
////        surveyAnswerResponseDto.getQuestionAnswerResponseDtoList().forEach(
////                questionAnswerResponseDto -> {
////                    questionIds.add(questionAnswerResponseDto.getId());
////                    questionAnswerResponseDto.getResponseAnswerMap().forEach(
////                            (s, integer) -> {
////                               responseAnswerMap.put(s,integer);
////                            }
////                    );
////                }
////        );
////
////        surveyResultList1.forEach(surveyResult -> {
////            Map<String,String> questionMap = surveyResult.getQuestionMap();
////            questionIds.forEach(s -> {
////                if(questionMap.containsKey(s)){
////                    System.out.println(s);
////                    System.out.println(questionMap.get(s));
////
////                    responseAnswerMap.forEach((s1, integer) -> {
////                        if(questionMap.get(s).contains(s1)){
////                            responseAnswerMap.put(s1,integer+1);
////                        }
////
////                    });
////                }
////
////            });
////            System.out.println(responseAnswerMap);
////
////        });
////
////        //Try01ENd
//
//
//        List<SurveyResult> surveyResultList = surveyResultRepository.findAll();
//        SurveyAnswerResponseDto surveyAnswerResponseDto = new SurveyAnswerResponseDto();
//
//        surveyResultList.stream().filter(surveyResult -> {
//            if (surveyResult.getSurvey().getId().equals(surveyId)) {
//
//                Integer responses = 0;
//                List<QuestionAnswerResponseDto> questionAnswerResponseDtoList = new ArrayList<>();
//
//                surveyResult.getSurvey().getQuestions().forEach(question -> {
//                    Map<String, Integer> responseAnswerMap = new HashMap<>();
//
//                    question.getOptions().forEach(answer -> {
//                        responseAnswerMap.put(answer, responses);
//                    });
//
//
//                    responseAnswerMap.forEach((s, integer) -> {
//                        if(surveyResult.getQuestionMap().containsValue(s)){
//                            responseAnswerMap.replace(s,responses,responses+1);
//                        }
//                    });
//
//                    System.out.println(responseAnswerMap);
//
//                    QuestionAnswerResponseDto questionAnswerResponseDto = new QuestionAnswerResponseDto();
//                    questionAnswerResponseDto.setId(question.getId());
//                    questionAnswerResponseDto.setText(question.getText());
//                    questionAnswerResponseDto.setResponseAnswerMap(responseAnswerMap);
//                    questionAnswerResponseDtoList.add(questionAnswerResponseDto);
//
//                });
//
//                surveyAnswerResponseDto.setId(surveyResult.getSurvey().getId());
//                surveyAnswerResponseDto.setSurveyName(surveyResult.getSurvey().getTitle());
//                surveyAnswerResponseDto.setQuestionAnswerResponseDtoList(questionAnswerResponseDtoList);
//                surveyAnswerResponseDto.setTotResponses(surveyResultList.size());
//
//
//                return true;
//            }
//            return false;
//        }).collect(Collectors.toList());
//
//
//        return surveyAnswerResponseDto;
//    }
//}

//    QuestionAnswerResponseDto questionAnswerResponseDto = new QuestionAnswerResponseDto();
//                    questionAnswerResponseDto.setText(question.getText());
//                            questionAnswerResponseDto.setId(question.getId());
//                            questionAnswerResponseDto.setResponseAnswerMap(responseAnswerMap);
//                            questionAnswerResponseDtoList.add(questionAnswerResponseDto);

//    public SurveyAnswerResponseDto getResponseData(String surveyId) {
//        List<SurveyResult> surveyResultList = surveyResultRepository.findAll();
//        List<String> surveyIds = new ArrayList<>();
//
//        SurveyAnswerResponseDto surveyAnswerResponseDto = new SurveyAnswerResponseDto();
//
//        Map<String, List<String>> questionResponseMap = new HashMap<>();
//
//        surveyResultList.stream()
//                .filter(surveyResult -> surveyResult.getSurvey().getId().equals(surveyId))
//                .forEach(surveyResult -> {
//                    surveyResult.getQuestionMap().forEach((questionId, selectedAnswer) -> {
//                        questionResponseMap.computeIfAbsent(questionId, k -> new ArrayList<>()).add(selectedAnswer);
//                    });
//                    surveyIds.add(surveyResult.getSurvey().getId());
//                });
//
//        List<QuestionAnswerResponseDto> questionAnswerResponseDtoList = new ArrayList<>();
//
//        for (Question question : surveyResultList.get(0).getSurvey().getQuestions()) {
//            List<String> responses = questionResponseMap.getOrDefault(question.getId(), Collections.emptyList());
//            Map<String, Integer> responseAnswerMap = new HashMap<>();
//
//            question.getOptions().forEach(answer -> {
//                int count = Collections.frequency(responses, answer);
//                responseAnswerMap.put(answer, count);
//            });
//
//            QuestionAnswerResponseDto questionAnswerResponseDto = new QuestionAnswerResponseDto();
//            questionAnswerResponseDto.setId(question.getId());
//            questionAnswerResponseDto.setText(question.getText());
//            questionAnswerResponseDto.setResponseAnswerMap(responseAnswerMap);
//            questionAnswerResponseDtoList.add(questionAnswerResponseDto);
//        }
//
//        surveyAnswerResponseDto.setId(surveyId);
//        surveyAnswerResponseDto.setSurveyName(surveyResultList.get(0).getSurvey().getTitle());
//        surveyAnswerResponseDto.setQuestionAnswerResponseDtoList(questionAnswerResponseDtoList);
//        surveyAnswerResponseDto.setTotResponses(surveyIds.size());
//
//        return surveyAnswerResponseDto;
//    }

    public SurveyAnswerResponseDto getResponseData(String surveyId) {
        List<SurveyResult> surveyResultList = surveyResultRepository.findAllBySurveyId(surveyId);
        SurveyAnswerResponseDto surveyAnswerResponseDto = new SurveyAnswerResponseDto();

        Map<String, Map<String, Integer>> questionResponseMap = new HashMap<>();

        for (SurveyResult surveyResult : surveyResultList) {
            surveyResult.getQuestionMap().forEach((questionId, selectedAnswer) -> {
                questionResponseMap.computeIfAbsent(questionId, k -> new HashMap<>())
                        .merge(selectedAnswer, 1, Integer::sum);
            });
        }

        List<QuestionAnswerResponseDto> questionAnswerResponseDtoList = new ArrayList<>();

        Survey survey = surveyRepository.findById(surveyId).get();
        for (Question question : survey.getQuestions()) {
            Map<String, Integer> responseAnswerMap = questionResponseMap.getOrDefault(question.getId(), new HashMap<>());

            QuestionAnswerResponseDto questionAnswerResponseDto = new QuestionAnswerResponseDto();
            questionAnswerResponseDto.setId(question.getId());
            questionAnswerResponseDto.setText(question.getText());
            questionAnswerResponseDto.setResponseAnswerMap(responseAnswerMap);
            questionAnswerResponseDtoList.add(questionAnswerResponseDto);
        }

        surveyAnswerResponseDto.setId(surveyId);
        surveyAnswerResponseDto.setSurveyName(survey.getTitle());
        surveyAnswerResponseDto.setQuestionAnswerResponseDtoList(questionAnswerResponseDtoList);
        surveyAnswerResponseDto.setTotResponses(surveyResultList.size());

        return surveyAnswerResponseDto;
    }


}