package com.hhgcl.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ForbiddenException extends RuntimeException {

    private boolean success;
    private HttpStatus httpStatus;
    public ForbiddenException(String message) {
        super(message);
        this.success = false;
        this.httpStatus = HttpStatus.FORBIDDEN;
    }
}
