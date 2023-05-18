package com.hsenid.surveyapp.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionAnswerResponseDto {

    String question;
    List<AnswerDto> answerDto;
}
