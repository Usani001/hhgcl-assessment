package com.hhgcl.service.service_impl;

import com.hhgcl.dtos.ApiResponse;
import com.hhgcl.dtos.request.UserLoginDto;
import com.hhgcl.dtos.request.UserRegisterDto;
import com.hhgcl.dtos.response.UserResponseDto;
import com.hhgcl.entity.User;
import com.hhgcl.exceptions.ForbiddenException;
import com.hhgcl.repository.UserRepository;
import com.hhgcl.security.CustomUserDetails;
import com.hhgcl.security.JwtService;
import com.hhgcl.service.AuthService;
import com.hhgcl.utils.Mappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final Mappers mappers;

    @Override
    @Transactional
    public ApiResponse<UserResponseDto> userSignup(UserRegisterDto request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new ForbiddenException("User already exist");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .role(request.getRole())
                .verified(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        User savedUser = userRepository.save(user);
        UserResponseDto response = mappers.userResponse(savedUser,"");
        response.setToken(null);
        return ApiResponse.success( "User registered successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<UserResponseDto> userLogin(UserLoginDto request) {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            User user = principal.getUser();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            String token = jwtService.generateToken(user);
            UserResponseDto response = mappers.userResponse(user, token);
            response.setToken(token);
            return ApiResponse.success("Login successful", response);
    }
}
