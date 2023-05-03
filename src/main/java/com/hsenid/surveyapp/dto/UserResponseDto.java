package com.hsenid.surveyapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserResponseDto {
    private String id;
    private String username;
    private String email;
}
