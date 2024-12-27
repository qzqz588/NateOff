package com.example.nateoff.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;

    private String name;

    private String password;

    private String phone;

    private String email;

    private String provider; // 소셜 제공자 이름 (예: google, kakao, naver)

    private String providerId; // 소셜 제공자가 제공하는 고유 ID

    @Column(nullable = false)
    private String role ="ROLE_USER";

    @Builder
    public User(String username, String password, String email, String phone, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
