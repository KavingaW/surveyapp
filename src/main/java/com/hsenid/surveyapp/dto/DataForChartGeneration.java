package com.hsenid.surveyapp.dto;

import lombok.*;

public @Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class DataForChartGeneration{
    SurveyAnswerResponseDto surveyResult;
}
