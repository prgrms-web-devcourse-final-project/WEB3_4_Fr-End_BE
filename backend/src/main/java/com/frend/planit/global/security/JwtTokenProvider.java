package com.frend.planit.global.security;

import com.frend.planit.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    // refresh token 만료 시간 getter (Redis TTL 설정용)
    public long getRefreshTokenExpirationMs() {
        return refreshTokenExpiration;
    }

    // refresh token 만료 임박시 재발급
    public Date getExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    // 토큰 내 권한(Role) 정보를 Security 권한 객체로 변환
    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = parseClaims(token);
        String role = claims.get("role", String.class);
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // Claims 파싱 메서드 (공통화)
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}