package com.hhgcl.utils;

import com.hhgcl.dtos.response.UserResponseDto;
import com.hhgcl.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MappersTest {

    private final Mappers mappers = new Mappers();

    @Test
    void userResponse_ShouldMapUserToResponseDto() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .role("USER")
                .build();

        // Act
        UserResponseDto response = mappers.userResponse(user, "token123");

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
        assertEquals("token123", response.getToken());
    }
}
