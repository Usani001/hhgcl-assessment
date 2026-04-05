package com.hhgcl.utils;

import com.hhgcl.dtos.response.UserResponseDto;
import com.hhgcl.entity.User;
import org.springframework.stereotype.Component;

@Component
public class Mappers {

    public UserResponseDto userResponse(User user,String token) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
            .build();
    }
}
