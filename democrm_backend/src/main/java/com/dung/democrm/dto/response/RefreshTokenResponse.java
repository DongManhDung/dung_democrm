package com.dung.democrm.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponse {
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
}
