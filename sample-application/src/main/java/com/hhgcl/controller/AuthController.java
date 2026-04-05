package com.hhgcl.controller;

import com.hhgcl.dtos.request.UserLoginDto;
import com.hhgcl.dtos.request.UserRegisterDto;
import com.hhgcl.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping({"/auth"})
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> userSignup(@RequestBody @Valid UserRegisterDto request) {
        var response = authService.userSignup(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody @Valid UserLoginDto request)
    {
        var response = authService.userLogin(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
