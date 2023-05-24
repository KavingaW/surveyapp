package com.hsenid.surveyapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SurveyListResponseDto {

    List<SurveyResponseDto>  surveyResponseDtoList;
}
