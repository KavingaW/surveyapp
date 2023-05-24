package com.hsenid.surveyapp.dto;

import com.hsenid.surveyapp.model.User;
import lombok.Builder;


import java.util.List;

@Builder
public class SurveyRequestDto {

    private String title;
    private String description;
    private List<QuestionRequestDto> questions;
    private List<UserRequestDto> assigned;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<QuestionRequestDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionRequestDto> questions) {
        this.questions = questions;
    }

    public List<UserRequestDto> getAssigned() {
        return assigned;
    }

    public void setAssigned(List<UserRequestDto> assigned) {
        this.assigned = assigned;
    }
}
