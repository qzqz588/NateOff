package com.example.nateoff.controller;

import com.example.nateoff.domain.dto.UserDto;
import com.example.nateoff.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) {
        System.out.println("recived data" + userDto);
            userService.register(userDto);
            return ResponseEntity.ok("회원가입 성공!");
        }
    }
