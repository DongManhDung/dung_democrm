package com.dung.democrm.service.impl;

import com.dung.democrm.common.exception.TokenExpiredException;
import com.dung.democrm.common.exception.UnauthorizedException;
import com.dung.democrm.entity.RefreshToken;
import com.dung.democrm.entity.User;
import com.dung.democrm.repository.RefreshTokenRepository;
import com.dung.democrm.service.RefreshTokenService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Dotenv dotenv;

    @Override
    public RefreshToken create(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);

        long refreshExpired = Long.parseLong(dotenv.get("JWT_REFRESH_EXPIRED"));
        refreshToken.setExpiredAt(LocalDateTime.now().plus(Duration.ofMillis(refreshExpired)));

        refreshToken.setLastUsedAt(LocalDateTime.now());
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verify(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> new UnauthorizedException("Refresh token is invalid."));

        if(refreshToken.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new TokenExpiredException("Refresh token is expired.");
        }

        return refreshToken;
    }

    @Override
    public void revoke(String token) {
        RefreshToken refreshToken = verify(token);
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void revokeAll(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserAndRevokedFalse(user);

        tokens.forEach(token -> token.setRevoked(true));

        refreshTokenRepository.saveAll(tokens);
    }

    @Override
    public void updateLastUsedAt(RefreshToken refreshToken) {
        refreshToken.setLastUsedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
    }
}
