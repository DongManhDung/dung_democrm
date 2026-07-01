package com.dung.democrm.service;

import com.dung.democrm.entity.RefreshToken;
import com.dung.democrm.entity.User;

public interface RefreshTokenService {
    // Tạo mới RefreshToken cho user
    RefreshToken create(User user);

    // Kiểm tra RefreshToken hợp lệ
    RefreshToken verify(String token);

    // Thu hồi RefreshToken
    void revoke(String token);

    // Thu hồi tất cả RefreshToken của user
    void revokeAll(User user);

    // Cập nhật thời gian sử dụng cuối cùng của RefreshToken
    void updateLastUsedAt(RefreshToken refreshToken);

}
