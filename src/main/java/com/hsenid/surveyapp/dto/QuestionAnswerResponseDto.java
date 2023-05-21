package com.hsenid.surveyapp.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionAnswerResponseDto {

    String id;
    String text;
    Map<String, Integer> responseAnswerMap;
}
