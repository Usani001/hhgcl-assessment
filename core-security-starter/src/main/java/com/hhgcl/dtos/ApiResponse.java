package com.hhgcl.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
@ToString
@JsonPropertyOrder({ "success", "message", "data"})
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    @JsonIgnore
    private HttpStatus httpStatus;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(boolean success, String message, HttpStatus httpStatus) {
        this.success = success;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ApiResponse(boolean success, String message, T data, HttpStatus httpStatus) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.httpStatus = httpStatus;
    }
}
