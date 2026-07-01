package com.dung.democrm.controller;

import com.dung.democrm.dto.request.LoginRequest;
import com.dung.democrm.dto.request.LogoutRequest;
import com.dung.democrm.dto.request.RefreshTokenRequest;
import com.dung.democrm.dto.response.CurrentUserResponse;
import com.dung.democrm.dto.response.LoginResponse;
import com.dung.democrm.dto.response.RefreshTokenResponse;
import com.dung.democrm.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(@Valid @RequestBody RefreshTokenRequest request){
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody LogoutRequest request){
        authService.logout(request);
    }

    @GetMapping("/me")
    public CurrentUserResponse me(
            Authentication authentication
    ){
        return authService.me(authentication);
    }
}
