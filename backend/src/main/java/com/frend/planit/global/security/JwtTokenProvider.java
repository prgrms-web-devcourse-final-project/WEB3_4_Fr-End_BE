package com.frend.planit.global.security;

import com.frend.planit.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration-ms}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration-ms}")
    private Long refreshTokenExpiration;

    // 토큰 생성
    public String createAccessToken(Long userId, Role role) {
        return createToken(userId, role, accessTokenExpiration);
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, null, refreshTokenExpiration);
    }

    private String createToken(Long userId, Role role, Long expireTimeMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireTimeMs);

        Claims claims = Jwts.claims().setSubject(userId.toString());
        if (role != null) {
            claims.put("role", role.name());
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 유저 ID 추출
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }
}