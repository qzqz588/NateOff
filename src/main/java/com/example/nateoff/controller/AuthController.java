package com.example.nateoff.controller;

import com.example.nateoff.domain.dto.LoginRequest;
import com.example.nateoff.domain.entity.CustomUserDetails;
import com.example.nateoff.domain.entity.User;
import com.example.nateoff.jwt.JwtTokenProvider;
import com.example.nateoff.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

    @RestController
    @Slf4j
    @RequestMapping("/api/auth")
    public class AuthController {
        private final AuthenticationManager authenticationManager;
        private final AuthService authService;
        private final JwtTokenProvider jwtTokenProvider;

        public AuthController(AuthenticationManager authenticationManager, AuthService authService, JwtTokenProvider jwtTokenProvider) {
            this.authenticationManager = authenticationManager;
            this.authService = authService;
            this.jwtTokenProvider = jwtTokenProvider;
        }

        /**
         * 일반 로그인 처리
         */
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
            try {
                // Authentication 객체 생성
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );

                // JWT 토큰 생성
                String token = jwtTokenProvider.generateToken(authentication);
                String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
                // JWT 쿠키 설정
                ResponseCookie jwtCookie = ResponseCookie.from("token", token)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(7 * 24 * 60 * 60) // 7일 유지
                        .sameSite("Strict")
                        .build();

                Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setPath("/");
                refreshTokenCookie.setMaxAge(24 * 60 * 60); // 1일

                return ResponseEntity.ok()
                        .header("Set-Cookie", jwtCookie.toString(),"Set-Cookie",refreshToken)
                        .body(Map.of(
                                "message", "Login successful",
                                "username", authentication.getName()
                        ));

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid username or password"));
            }
        }


        @PostMapping("/logout")
        public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
            try {
                // 쿠키 삭제
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("token".equals(cookie.getName()) || "refreshToken".equals(cookie.getName())) {
                            Cookie newCookie = new Cookie(cookie.getName(), null);
                            newCookie.setPath("/");
                            newCookie.setMaxAge(0);
                            newCookie.setHttpOnly(true);
                            response.addCookie(newCookie);
                        }
                    }
                }

                // 시큐리티 컨텍스트 클리어
                SecurityContextHolder.clearContext();

                // 응답 데이터
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Successfully logged out");

                return ResponseEntity.ok(responseData);
            } catch (Exception e) {
                log.error("로그아웃 처리 중 오류 발생", e);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "로그아웃 처리 중 오류가 발생했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
        @GetMapping("/status")
        public ResponseEntity<Map<String, Boolean>> getStatus(HttpServletRequest request) {
            Cookie[] cookies = request.getCookies();
            boolean loggedIn = false;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        loggedIn = true;
                        System.out.println(loggedIn);
                        break;
                    }
                }
            }

            return ResponseEntity.ok(Map.of("loggedIn", loggedIn));
        }
    }