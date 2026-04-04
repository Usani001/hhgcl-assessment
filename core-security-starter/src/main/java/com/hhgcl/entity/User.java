package com.hhgcl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(name = "session_id")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role")
    private String role;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password = "";

    @Column(name = "verified")
    private boolean verified = false;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

}
