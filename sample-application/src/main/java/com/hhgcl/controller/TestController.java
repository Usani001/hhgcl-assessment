package com.hhgcl.controller;

import com.hhgcl.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/public/health")
    public ResponseEntity<?> health() {
        var response = testService.health();
        return new ResponseEntity<>(response, response.getHttpStatus());
    }


    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser() {
        var response = testService.getCurrentUser();
        return new ResponseEntity<>(response, response.getHttpStatus());
    }


    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "createdAt") String sortBy, @RequestParam(defaultValue = "DESC") String sortOrder) {
        var response = testService.getAllUsers(page, limit, sortBy, sortOrder);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
