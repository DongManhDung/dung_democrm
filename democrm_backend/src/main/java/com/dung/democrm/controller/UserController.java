package com.dung.democrm.controller;

import com.dung.democrm.dto.request.*;
import com.dung.democrm.dto.response.TeamMemberResponse;
import com.dung.democrm.dto.response.UserDetailResponse;
import com.dung.democrm.dto.response.UserResponse;
import com.dung.democrm.service.RefreshTokenService;
import com.dung.democrm.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private final RefreshTokenService refreshTokenService;

    @GetMapping
    public Page<UserResponse> getAll(
            UserSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
            ){
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return userService.getAll(request, pageable);
    }

    @GetMapping("/{id}")
    public UserDetailResponse getById(@PathVariable Long id){
        return userService.getById(id);
    }

    @PostMapping
    public UserDetailResponse create(@Valid @RequestBody CreateUserRequest request){
        return userService.create(request);
    }

    @PutMapping("/{id}")
    public UserDetailResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request){
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }

    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody ResetPasswordRequest request
            ){
        userService.resetPassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeUserStatusRequest request,
            Authentication authentication
            ){
        userService.changeStatus(id, request, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-team")
    public List<TeamMemberResponse> getMyTeam(Authentication authentication){
        return userService.getMyTeam(authentication);
    }
}
