package com.hhgcl.service;

import com.hhgcl.dtos.ApiResponse;
import com.hhgcl.dtos.request.UserLoginDto;
import com.hhgcl.dtos.request.UserRegisterDto;
import com.hhgcl.dtos.response.UserResponseDto;
import com.instollar.user_service.dto.request.UserDto;
import com.instollar.user_service.dto.request.UserLoginDto;
import com.instollar.user_service.dto.request.UserRegisterDto;
import com.instollar.user_service.dto.response.ApiResponse;
import com.instollar.user_service.dto.response.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    ApiResponse<UserResponseDto> userSignup(UserRegisterDto request);
    ApiResponse<UserResponseDto> userLogin(UserLoginDto request);


}
