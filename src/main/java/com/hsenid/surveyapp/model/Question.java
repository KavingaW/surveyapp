package com.hsenid.surveyapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "question")
public class Question {
    @Id
    private String id;
    private String text;
    private String type;
    private List<String> options;

    public Question() {
    }

    public Question(String id, String text, String type, List<String> options) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
