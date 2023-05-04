package com.hsenid.surveyapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DeleteResponseDto {

    private String code;
    private String message;

}
