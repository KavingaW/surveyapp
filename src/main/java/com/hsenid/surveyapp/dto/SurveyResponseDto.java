package com.hsenid.surveyapp.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseDto {
    private String title;
    private String description;
    private List<QuestionResponseDto> questions;
    private List<String> assigned;
}
