package com.hsenid.surveyapp.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class QuestionResponseDto {

    private String text;
    private String type;
    private List<String> options;

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
