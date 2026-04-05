package com.hhgcl.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@JsonPropertyOrder({ "success", "message", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private int status;
    private String message;
    private T data;
    private String timestamp;
    private Pagination pagination;

    @JsonIgnore
    private HttpStatus httpStatus;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now().toString())
                .build();

    }

    public static <T> ApiResponse<T> paged(String message, T data, Pagination pagination) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message(message)
                .data(data)
                .pagination(pagination)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
