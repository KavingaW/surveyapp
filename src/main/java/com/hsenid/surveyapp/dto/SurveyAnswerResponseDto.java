package com.hsenid.surveyapp.dto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnswerResponseDto {
    String id;
    String surveyName;
    List<String> users;
    List<QuestionAnswerResponseDto> questionAnswerResponseDtoList;
    Integer totResponses;
}
