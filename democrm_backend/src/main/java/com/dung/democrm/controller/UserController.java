package com.dung.democrm.controller;

import com.dung.democrm.dto.request.CreateUserRequest;
import com.dung.democrm.dto.request.UpdateUserRequest;
import com.dung.democrm.dto.response.UserDetailResponse;
import com.dung.democrm.dto.response.UserResponse;
import com.dung.democrm.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Page<UserResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        return userService.getAll(pageable);
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
}
