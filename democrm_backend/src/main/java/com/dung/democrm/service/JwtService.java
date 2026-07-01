package com.dung.democrm.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final Dotenv dotenv;

    // Create Access Token
    public String generateAccessToken(String email){
        return generateToken(Map.of(), email, Long.parseLong(dotenv.get("JWT_ACCESS_EXPIRED")));
    }

    // Create Refresh Token
    public String generateRefreshToken(String email){
        return generateToken(Map.of(), email, Long.parseLong(dotenv.get("JWT_REFRESH_EXPIRED")));
    }



    // Sinh JWT
    private String generateToken(Map<String, Object> extraClaims, String subject, long expiration){
        Date now = new Date();
        return Jwts.builder().claims(extraClaims).subject(subject).issuedAt(now).
                expiration(new Date(now.getTime() + expiration)).compact();
    }

    // Lấy email trong JWT
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Lấy Expiration trong JWT
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    // Lấy Claims trong JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Kiểm tra token hết hạn
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Validate JWT
    public boolean isTokenValid(String token, String email){
        String username = extractUsername(token);
        return username.equals(email) && !isTokenExpired(token);
    }

    // Parse Claims
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Secret Key
    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(dotenv.get("JWT_SECRET"));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
