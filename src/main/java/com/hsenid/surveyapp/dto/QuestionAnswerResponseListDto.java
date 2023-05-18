package com.hsenid.surveyapp.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerResponseListDto {
    List<QuestionAnswerResponseDto> questionAnswerResponseDtoList;
}
