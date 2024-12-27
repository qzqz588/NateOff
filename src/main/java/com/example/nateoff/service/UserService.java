package com.example.nateoff.service;

import com.example.nateoff.domain.dto.UserDto;
import com.example.nateoff.domain.entity.User;
import com.example.nateoff.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void register(UserDto userDto) {
        if (existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByPhoneAndEmail(userDto.getPhone(), userDto.getEmail())) {
            throw new RuntimeException("Phone or Email already exists");
        }
        User user = User.builder()
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .username(userDto.getUsername())
                .phone(userDto.getPhone())
                .role("ROLE_USER")
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        userRepository.save(user);
    }
    //Username(id) 중복 확인
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    //전화번호,이메일 중복 확인
    public boolean existsByPhoneAndEmail(String phone, String email) {
        return userRepository.findByPhoneAndEmail(phone, email);
    }

    //유저 정보를 반환
    public User findByUserName(String username) {
        log.info("Finding user by userId: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with userId: {}", username);
                    return new RuntimeException("User not found with userId: " + username);
                });
    }
}
