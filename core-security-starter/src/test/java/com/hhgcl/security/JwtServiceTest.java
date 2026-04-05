package com.hhgcl.security;

import com.hhgcl.entity.User;
import com.hhgcl.repository.UserRepository;
import com.hhgcl.utils.SecurityProperties;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityProperties properties;

    @InjectMocks
    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testuser")
                .role("USER")
                .build();
        user.setId("1");

        lenient().when(properties.getSecret()).thenReturn("dGVzdC1zZWNyZXQtZm9yLWp3dC10b2tlbi1nZW5lcmF0aW9uLWFuZC12YWxpZGF0aW9u"); // base64 encoded "test-secret-for-jwt-token-generation-and-validation"
        lenient().when(properties.getExpiration()).thenReturn(3600000L); // 1 hour
    }

    @Test
    void generateToken_ShouldGenerateValidToken() {
        // Act
        String token = jwtService.generateToken(user);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractClaims_ShouldExtractClaimsFromToken() {
        // Arrange
        String token = jwtService.generateToken(user);

        // Act
        Claims claims = jwtService.extractClaims(token);

        // Assert
        assertNotNull(claims);
        assertEquals("testuser", claims.getSubject());
        assertEquals("1", claims.get("userId"));
        assertEquals(List.of("USER"), claims.get("roles"));
        assertEquals("testuser", claims.get("username"));
    }

    @Test
    void isTokenValid_ShouldReturnTrue_ForValidToken() {
        // Arrange
        String token = jwtService.generateToken(user);

        // Act
        boolean isValid = jwtService.isTokenValid(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_ForInvalidToken() {
        // Act
        boolean isValid = jwtService.isTokenValid("invalid.token.here");

        // Assert
        assertFalse(isValid);
    }
}
