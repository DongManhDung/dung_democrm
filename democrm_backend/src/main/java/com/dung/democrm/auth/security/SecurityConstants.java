package com.dung.democrm.auth.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityConstants {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    // API Prefix
    public static final String AUTH_API= "/api/auth/**";

    // Token Expiration
    public static final long ACCESS_TOKEN_EXPIRED = 1000L * 60 * 30; //30 minutes
    public static final long REFRESH_TOKEN_EXPIRED = 1000L * 60 * 60 * 24 * 7; //7 days

}
