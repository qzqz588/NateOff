package com.example.nateoff.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey jwtSecretKey;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    @Value("${jwt.refresh.expiration}")
    private int refreshTokenExpirationMs;

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        this.jwtSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    // JWT 생성
    public String generateToken(Authentication authentication) {
        String username;
        Map<String, Object> claims = new HashMap<>();

        // OAuth2 사용자 처리
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String provider = getProvider(oauth2User);
            String providerId = getProviderId(oauth2User);
            username = provider + "_" + providerId; // 고유 username 생성 (ex: google_123456)

            claims.put("provider", provider); // OAuth2 제공자 정보
            claims.put("type", "OAUTH");     // 사용자 유형(OAuth2)
        }
        // 일반 사용자 처리
        else {
            username = authentication.getName(); // 일반 사용자의 username
            claims.put("type", "NORMAL");        // 사용자 유형(일반 로그인)

            // 사용자 역할 추가
            claims.put("roles", authentication.getAuthorities()
                    .stream()
                    .map(auth -> auth.getAuthority())
                    .toList());
        }

        // 디버깅 로그
        log.info("Generating token for username: {}", username);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // JWT 생성
        return Jwts.builder()
                .setClaims(claims)    // 추가 정보
                .setSubject(username) // username을 subject로 설정
                .setIssuedAt(now)     // 발급 시간
                .setExpiration(expiryDate) // 만료 시간
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    private String getProvider(OAuth2User oauth2User) {
        // OAuth2AuthenticationToken의 RegistrationId를 기반으로 제공자 이름 반환
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return authToken.getAuthorizedClientRegistrationId(); // ex: "google", "kakao", "naver"
    }

    // Refresh Token 생성
    public String generateRefreshToken(Authentication authentication) {
        String userId;
        Map<String, Object> claims = new HashMap<>();

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String provider = getProvider(oauth2User);
            String providerId = getProviderId(oauth2User);
            userId = provider + "_" + providerId;
            claims.put("type", "OAUTH_REFRESH");
        } else {
            userId = authentication.getName();
            claims.put("type", "NORMAL_REFRESH");
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);

        String refreshToken = Jwts.builder()
                .setClaims(claims)  // claims를 먼저 설정
                .setSubject(userId) // subject는 claims 이후에 설정
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();

        log.info("Generated Refresh Token: {}", refreshToken);
        return refreshToken;
    }
    private String getProviderId(OAuth2User oauth2User) {
        // 각 제공자의 고유 ID 반환
        Map<String, Object> attributes = oauth2User.getAttributes();
        String provider = getProvider(oauth2User);

        switch (provider) {
            case "google":
                return (String) attributes.get("sub"); // Google의 사용자 고유 ID
            case "kakao":
                return String.valueOf(attributes.get("id")); // Kakao의 사용자 고유 ID
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return (String) response.get("id"); // Naver의 사용자 고유 ID
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }


    // JWT에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }



    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT expired: " + e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
        }
        return false;
    }
}

