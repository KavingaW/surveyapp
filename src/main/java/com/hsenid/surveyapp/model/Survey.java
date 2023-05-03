package com.hsenid.surveyapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "survey")
public class Survey {
    @Id
    private String id;
    private String title;
    private String description;
    @DBRef
    private List<Question> questions;
    private List<String> assignedTo;

    public Survey() {
    }

    public Survey(String id, String title, String description, List<Question> questions, List<String> assignedTo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.assignedTo = assignedTo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<String> getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(List<String> assignedTo) {
        this.assignedTo = assignedTo;
    }
}
