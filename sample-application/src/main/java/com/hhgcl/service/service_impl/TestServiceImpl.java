package com.hhgcl.service.service_impl;

import com.hhgcl.dtos.ApiResponse;
import com.hhgcl.dtos.Pagination;
import com.hhgcl.dtos.response.UserResponseDto;
import com.hhgcl.entity.User;
import com.hhgcl.repository.UserRepository;
import com.hhgcl.security.CustomUserDetails;
import com.hhgcl.service.TestService;
import com.hhgcl.utils.Mappers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final UserRepository userRepository;
    private final Mappers mappers;


    @Override
    public ApiResponse<String> health() {
        return ApiResponse.success("Service is up and running", "OK");
    }

    @Override
    public ApiResponse<UserResponseDto> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = principal.getUser();

        UserResponseDto response = mappers.userResponse(user, "");

        return ApiResponse.success("User retrieved successfully", response);
    }


    @Override
    public ApiResponse<List<UserResponseDto>> getAllUsers(int page, int limit, String sortBy, String sortOrder) {

        Sort sort = Sort.by(
                Sort.Direction.fromString(sortOrder == null ? "DESC" : sortOrder),
                sortBy == null ? "createdAt" : sortBy
        );
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<User> users = userRepository.findAll(pageable);

        List<UserResponseDto> responses = users.stream()
                .map(user -> mappers.userResponse(user, ""))
                .toList();

        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setLimit(limit);
        pagination.setTotal(users.getTotalElements());
        pagination.setTotalPages(users.getTotalPages());
        pagination.setHasNext(users.hasNext());
        pagination.setHasPrev(users.hasPrevious());
        pagination.setNextPage(users.hasNext() ? page + 1 : null);
        pagination.setPrevPage(users.hasPrevious() ? page - 1 : null);
        pagination.setSortBy(sortBy != null ? sortBy : "createdAt");
        pagination.setSortOrder(sortOrder != null ? sortOrder : "desc");
        return ApiResponse.paged("Users retrieved successfully", responses, pagination);
    }
}
