package com.hsenid.surveyapp.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class QuestionListResponseDto {

    List<QuestionResponseDto> questionResponseDtoList;
}
