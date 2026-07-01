package com.dung.democrm.service.impl;

import com.dung.democrm.entity.RefreshToken;
import com.dung.democrm.service.JwtService;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.LoginRequest;
import com.dung.democrm.dto.request.LogoutRequest;
import com.dung.democrm.dto.request.RefreshTokenRequest;
import com.dung.democrm.dto.response.CurrentUserResponse;
import com.dung.democrm.dto.response.LoginResponse;
import com.dung.democrm.dto.response.RefreshTokenResponse;
import com.dung.democrm.entity.User;
import com.dung.democrm.repository.UserRepository;
import com.dung.democrm.service.AuthService;
import com.dung.democrm.service.RefreshTokenService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final Dotenv dotenv;

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        String accessToken = jwtService.generateAccessToken(user.getEmail());

        RefreshToken refreshToken = refreshTokenService.create(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(Long.parseLong(dotenv.get("JWT_ACCESS_EXPIRED")))
                .user(
                        LoginResponse.UserInfo.builder()
                                .id(user.getId())
                                .employeeCode(user.getEmployeeCode())
                                .fullName(user.getFullName())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .status(user.getStatus())
                                .build()
                )
                .build();
    }

    @Override
    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verify(request.getRefreshToken());

        refreshTokenService.updateLastUsedAt(refreshToken);

        String accessToken = jwtService.generateAccessToken(refreshToken.getUser().getEmail());

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(Long.parseLong(dotenv.get("JWT_ACCESS_EXPIRED")))
                .build();
    }

    @Override
    public void logout(LogoutRequest request) {
        refreshTokenService.revoke(request.getRefreshToken());
    }

    @Override
    public CurrentUserResponse me(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + authentication.getName()));

        return CurrentUserResponse.builder()
                .id(user.getId())
                .employeeCode(user.getEmployeeCode())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .managerId(
                        user.getManager() != null ? user.getManager().getId() : null
                )
                .managerName(
                        user.getManager() != null ? user.getManager().getFullName() : null
                )
                .build();
    }
}
