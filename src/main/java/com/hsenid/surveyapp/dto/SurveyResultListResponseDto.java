package com.hsenid.surveyapp.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResultListResponseDto {

    private List<SurveyResultResponseDto> surveyResultListResponseDtoList;
    private Integer respondents;
}
