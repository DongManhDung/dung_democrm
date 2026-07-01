package com.dung.democrm.service;

import com.dung.democrm.dto.request.LoginRequest;
import com.dung.democrm.dto.request.LogoutRequest;
import com.dung.democrm.dto.request.RefreshTokenRequest;
import com.dung.democrm.dto.response.CurrentUserResponse;
import com.dung.democrm.dto.response.LoginResponse;
import com.dung.democrm.dto.response.RefreshTokenResponse;
import com.dung.democrm.entity.User;
import org.springframework.security.core.Authentication;

public interface AuthService {
    // Login
    LoginResponse login(LoginRequest request);

    // Refresh access token
    RefreshTokenResponse refresh(RefreshTokenRequest request);

    // Logout
    void logout(LogoutRequest request);

    // Lấy thông tin người dùng hiện tại
    CurrentUserResponse me(Authentication authentication);
}
