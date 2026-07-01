package com.dung.democrm.repository;

import com.dung.democrm.entity.RefreshToken;
import com.dung.democrm.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);
    List<RefreshToken> findAllByUserAndRevokedFalse(User user);
    boolean existsByToken(String token);
    List<RefreshToken> findByExpiredAtBefore(LocalDateTime expiredAt);
}
