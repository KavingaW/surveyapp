package com.hsenid.surveyapp.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequestDto {
    private String id;
    private String text;
    private String type;
    private List<String> options;
}
