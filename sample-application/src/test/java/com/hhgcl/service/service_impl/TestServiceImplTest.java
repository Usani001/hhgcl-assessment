package com.hhgcl.service.service_impl;

import com.hhgcl.dtos.ApiResponse;
import com.hhgcl.dtos.Pagination;
import com.hhgcl.dtos.response.UserResponseDto;
import com.hhgcl.entity.User;
import com.hhgcl.repository.UserRepository;
import com.hhgcl.security.CustomUserDetails;
import com.hhgcl.utils.Mappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Mappers mappers;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails customUserDetails;

    @InjectMocks
    private TestServiceImpl testService;

    private User user;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .role("USER")
                .build();
        user.setId("1");

        userResponseDto = UserResponseDto.builder()
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .role("USER")
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void health_ShouldReturnSuccessResponse() {
        // Act
        ApiResponse<String> response = testService.health();

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Service is up and running", response.getMessage());
        assertEquals("OK", response.getData());
    }

    @Test
    void getCurrentUser_ShouldReturnCurrentUser() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(customUserDetails.getUser()).thenReturn(user);
        when(mappers.userResponse(any(User.class), anyString())).thenReturn(userResponseDto);

        // Act
        ApiResponse<UserResponseDto> response = testService.getCurrentUser();

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("User retrieved successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("testuser", response.getData().getUsername());
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(mappers).userResponse(user, "");
    }

    @Test
    void getAllUsers_ShouldReturnPagedUsers() {
        // Arrange
        List<User> users = List.of(user);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")), 1);
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
        when(mappers.userResponse(any(User.class), anyString())).thenReturn(userResponseDto);

        // Act
        ApiResponse<List<UserResponseDto>> response = testService.getAllUsers(1, 10, "createdAt", "DESC");

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Users retrieved successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertNotNull(response.getPagination());
        assertEquals(1, response.getPagination().getPage());
        assertEquals(10, response.getPagination().getLimit());
        assertEquals(1L, response.getPagination().getTotal());
        assertEquals(1, response.getPagination().getTotalPages());
        assertFalse(response.getPagination().isHasNext());
        assertFalse(response.getPagination().isHasPrev());
        verify(userRepository).findAll(any(PageRequest.class));
        verify(mappers).userResponse(user, "");
    }

    @Test
    void getAllUsers_ShouldHandleNullSortByAndSortOrder() {
        // Arrange
        List<User> users = List.of(user);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")), 1);
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
        when(mappers.userResponse(any(User.class), anyString())).thenReturn(userResponseDto);

        // Act
        ApiResponse<List<UserResponseDto>> response = testService.getAllUsers(1, 10, null, null);

        // Assert
        assertTrue(response.isSuccess());
        assertNotNull(response.getPagination());
        assertEquals("createdAt", response.getPagination().getSortBy());
        assertEquals("desc", response.getPagination().getSortOrder());
    }
}
