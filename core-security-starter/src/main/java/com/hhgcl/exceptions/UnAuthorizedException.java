package com.hhgcl.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class UnAuthorizedException extends RuntimeException {

  private boolean success;
  private HttpStatus httpStatus;
  public UnAuthorizedException(String message) {
    super(message);
    this.success = false;
    this.httpStatus = HttpStatus.UNAUTHORIZED;
  }
}
