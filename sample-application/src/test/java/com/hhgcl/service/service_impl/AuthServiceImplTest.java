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
import com.hhgcl.utils.Mappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Mappers mappers;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserRegisterDto registerDto;
    private UserLoginDto loginDto;
    private User user;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        registerDto = new UserRegisterDto();
        registerDto.setUsername("testuser");
        registerDto.setFirstName("Test");
        registerDto.setLastName("User");
        registerDto.setEmail("test@example.com");
        registerDto.setRole("USER");
        registerDto.setPassword("Password1");

        loginDto = new UserLoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("Password1");

        user = User.builder()
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .role("USER")
                .password("encodedPassword")
                .verified(true)
                .build();
        user.setId("1");

        userResponseDto = UserResponseDto.builder()
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .role("USER")
                .token("jwtToken")
                .build();
    }

    @Test
    void userSignup_ShouldRegisterUser_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mappers.userResponse(any(User.class), anyString())).thenReturn(userResponseDto);

        // Act
        ApiResponse<UserResponseDto> response = authService.userSignup(registerDto);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("User registered successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("testuser", response.getData().getUsername());
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).encode("Password1");
        verify(userRepository).save(any(User.class));
        verify(mappers).userResponse(any(User.class), eq(""));
    }

    @Test
    void userSignup_ShouldThrowForbiddenException_WhenUserAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> authService.userSignup(registerDto));
        assertEquals("User already exist", exception.getMessage());
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void userLogin_ShouldLoginUserSuccessfully() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(customUserDetails.getUser()).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(mappers.userResponse(any(User.class), anyString())).thenReturn(userResponseDto);

        // Act
        ApiResponse<UserResponseDto> response = authService.userLogin(loginDto);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("jwtToken", response.getData().getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(user);
        verify(mappers).userResponse(user, "jwtToken");
    }
}
