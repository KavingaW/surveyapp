package com.hsenid.surveyapp.controller;

import com.hsenid.surveyapp.dto.*;
import com.hsenid.surveyapp.service.AuthenticationService;
import com.hsenid.surveyapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//cross
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        authenticationService.authenticateUser(loginRequest);
        return ResponseEntity.ok(authenticationService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(new MessageResponse(authenticationService.registerUser(signUpRequest)));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody final ResetPasswordRequestDto resetPasswordRequestDto) {
        return new ResponseEntity(userService.resetPassword(resetPasswordRequestDto), HttpStatus.OK);
    }
}
