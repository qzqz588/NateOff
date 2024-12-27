package com.example.nateoff.controller;

import com.example.nateoff.domain.entity.User;
import com.example.nateoff.domain.repository.UserRepository;
import com.example.nateoff.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OAuth2Controller {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2Controller(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/callback/{provider}")
    public ResponseEntity<?> oauthCallback(@PathVariable String provider, OAuth2AuthenticationToken authentication) {
        // OAuth2User로부터 사용자 정보 가져오기
        OAuth2User oauth2User = authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        String providerId;
        String email;
        String name;

        // 플랫폼별 데이터 처리
        switch (provider) {
            case "google":
                providerId = (String) attributes.get("sub");
                email = (String) attributes.get("email");
                name = (String) attributes.get("name");
                break;

            case "kakao":
                providerId = String.valueOf(attributes.get("id"));
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                email = (String) kakaoAccount.get("email");
                name = (String) profile.get("nickname");
                break;

            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                providerId = (String) response.get("id");
                email = (String) response.get("email");
                name = (String) response.get("name");
                break;

            default:
                return ResponseEntity.badRequest().body("지원하지 않는 OAuth2 플랫폼입니다.");
        }
        // username 생성
        String username = provider + "_" + providerId; // ex) "kakao_123456789"
        // 사용자 정보 저장 또는 업데이트
        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> userRepository.save(User.builder()
                        .provider(provider)
                        .providerId(providerId)
                        .email(email)
                        .name(name)
                        .username(username)
                        .role("ROLE_USER")
                        .build()));

        // JWT 토큰 생성
        boolean rememberMe = true; // OAuth2 로그인은 Remember Me 기본 활성화
        String token = jwtTokenProvider.generateToken(authentication);

        // 클라이언트로 응답 반환
        return ResponseEntity.ok(Map.of(
                "message", "로그인 성공",
                "token", token,
                "email", user.getEmail(),
                "name", user.getName()
        ));
    }
}
