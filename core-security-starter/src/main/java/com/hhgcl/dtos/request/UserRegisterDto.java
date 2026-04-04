package com.hhgcl.dtos.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRegisterDto {

    private String username;

    private String firstName;

    private String lastName;

    private String role;

    private String email;

}
