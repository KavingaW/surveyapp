package com.hsenid.surveyapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SurveyListResponseDto {

    List<SurveyResponseDto>  surveyResponseDtoList;
}
