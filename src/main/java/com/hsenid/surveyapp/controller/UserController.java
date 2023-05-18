package com.hsenid.surveyapp.controller;

import com.hsenid.surveyapp.dto.DeleteResponseDto;
import com.hsenid.surveyapp.dto.ResetPasswordRequestDto;
import com.hsenid.surveyapp.dto.UserDto;
import com.hsenid.surveyapp.dto.UserResponseDto;
import com.hsenid.surveyapp.model.User;
import com.hsenid.surveyapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "id") final String userId, @RequestBody final UserDto userDto) {
        return new ResponseEntity<>(userService.updateUser(userId, userDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable(value = "id") final String userId) {
        UserDto userDto = userService.getUserById(userId);
        UserResponseDto userResponseDto = UserResponseDto.builder().id(userDto.getId()).username(userDto.getUsername()).email(userDto.getEmail()).build();
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserByName(@PathVariable(value = "name") final String userName) {
        return new ResponseEntity<>(userService.getUserByName(userName), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DeleteResponseDto> deleteUser(@PathVariable(value = "id") final String userId) {
        userService.deleteUser(userId);
        DeleteResponseDto responseDto = DeleteResponseDto.builder().code("200").message("Deleted").build();
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> userResponseDtoList = userService.getAllUsers();
        return new ResponseEntity(userResponseDtoList, HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> resetPassword(@RequestBody final ResetPasswordRequestDto resetPasswordRequestDto) {
        return new ResponseEntity(userService.resetPassword(resetPasswordRequestDto), HttpStatus.OK);
    }
}
