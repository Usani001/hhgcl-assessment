package com.hhgcl.service;

import com.hhgcl.dtos.ApiResponse;
import com.hhgcl.dtos.response.UserResponseDto;

import java.util.List;

public interface TestService {

    ApiResponse<String> health();

    ApiResponse<UserResponseDto> getCurrentUser();

    ApiResponse<List<UserResponseDto>> getAllUsers(int page, int limit, String sortBy, String sortOrder);
}
