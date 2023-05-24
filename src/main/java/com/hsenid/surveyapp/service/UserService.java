package com.hsenid.surveyapp.service;

import com.hsenid.surveyapp.dto.ResetPasswordRequestDto;
import com.hsenid.surveyapp.dto.UserDto;
import com.hsenid.surveyapp.dto.UserResponseDto;
import com.hsenid.surveyapp.model.User;

import java.util.List;

public interface UserService {

    public UserDto updateUser(final String userId, final UserDto userDto);

    public UserDto getUserById(final String userId);

    public UserDto getUserByName(final String userName);

    public List<UserResponseDto> getAllUsers();

    public String deleteUser(final String userId);

    public String resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);
}
