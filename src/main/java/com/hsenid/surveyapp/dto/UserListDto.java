package com.hsenid.surveyapp.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class UserListDto {
    List<UserDto> users;

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }


}
