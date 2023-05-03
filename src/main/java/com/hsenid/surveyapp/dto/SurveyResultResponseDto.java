package com.hsenid.surveyapp.dto;

import lombok.*;


import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResultResponseDto {
    private String userId;
    private String surveyId;
    private Map<String, String> questionMap;
    private Integer numberOfAnswers;
}
