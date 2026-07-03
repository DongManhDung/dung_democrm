package com.dung.democrm.service;

import com.dung.democrm.dto.request.CreateUserRequest;
import com.dung.democrm.dto.request.UpdateUserRequest;
import com.dung.democrm.dto.response.UserDetailResponse;
import com.dung.democrm.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    // Danh sách User
    Page<UserResponse> getAll(Pageable pageable);

    // Chi tiết User
    UserDetailResponse getById(Long id);

    // Tạo user
    UserDetailResponse create(CreateUserRequest request);

    // Cập nhật
    UserDetailResponse update(Long id, UpdateUserRequest request);

    // Soft delete
    void delete(Long id);

}
