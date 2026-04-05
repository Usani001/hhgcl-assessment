package com.hhgcl.security;

import com.hhgcl.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void constructor_ShouldInitializeFieldsFromUser() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .password("password")
                .role("USER")
                .verified(true)
                .build();

        // Act
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // Assert
        assertEquals(user, customUserDetails.getUser());
        assertEquals("test@example.com", customUserDetails.getEmail());
        assertEquals("testuser", customUserDetails.getUsername());
        assertEquals("Test", customUserDetails.getFirstName());
        assertEquals("User", customUserDetails.getLastName());
        assertEquals("password", customUserDetails.getPassword());
        assertTrue(customUserDetails.getVerified());
        assertNotNull(customUserDetails.getAuthorities());
        assertEquals(1, customUserDetails.getAuthorities().size());
        assertEquals("USER", customUserDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void getAuthorities_ShouldReturnAuthorities() {
        // Arrange
        User user = User.builder().role("ADMIN").build();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // Act
        var authorities = customUserDetails.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
    }

    @Test
    void getPassword_ShouldReturnPassword() {
        // Arrange
        User user = User.builder().password("pass").role("USER").build();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // Act
        String password = customUserDetails.getPassword();

        // Assert
        assertEquals("pass", password);
    }

    @Test
    void getUsername_ShouldReturnUsername() {
        // Arrange
        User user = User.builder().username("user").role("USER").build();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // Act
        String username = customUserDetails.getUsername();

        // Assert
        assertEquals("user", username);
    }

    @Test
    void isAccountNonExpired_ShouldReturnTrue() {
        // Arrange
        CustomUserDetails customUserDetails = new CustomUserDetails(User.builder().role("USER").build());

        // Act
        boolean result = customUserDetails.isAccountNonExpired();

        // Assert
        assertTrue(result);
    }

    @Test
    void isAccountNonLocked_ShouldReturnTrue() {
        // Arrange
        CustomUserDetails customUserDetails = new CustomUserDetails(User.builder().role("USER").build());

        // Act
        boolean result = customUserDetails.isAccountNonLocked();

        // Assert
        assertTrue(result);
    }

    @Test
    void isCredentialsNonExpired_ShouldReturnTrue() {
        // Arrange
        CustomUserDetails customUserDetails = new CustomUserDetails(User.builder().role("USER").build());

        // Act
        boolean result = customUserDetails.isCredentialsNonExpired();

        // Assert
        assertTrue(result);
    }

    @Test
    void isEnabled_ShouldReturnTrue() {
        // Arrange
        CustomUserDetails customUserDetails = new CustomUserDetails(User.builder().role("USER").build());

        // Act
        boolean result = customUserDetails.isEnabled();

        // Assert
        assertTrue(result);
    }
}
