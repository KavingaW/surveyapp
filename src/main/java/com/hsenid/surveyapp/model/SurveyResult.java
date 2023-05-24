package com.hsenid.surveyapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "survey_result")
public class SurveyResult {

    @Id
    private String id;
    @DBRef
    private User user;
    @DBRef
    private Survey survey;
    private Map<String,String> questionMap;

    public SurveyResult() {
    }

    public SurveyResult(String id, User userId, Survey surveyId, Map<String, String> questionMap) {
        this.id = id;
        this.user = userId;
        this.survey = surveyId;
        this.questionMap = questionMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Map<String, String> getQuestionMap() {
        return questionMap;
    }

    public void setQuestionMap(Map<String, String> questionMap) {
        this.questionMap = questionMap;
    }
}
