package com.example.nateoff.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDto {
    private Long requestId; //Friend엔티티의 id
    private Long userId; // 요청 보낸 사용자
    private Long friendId; // 요청 받은 사용자
    private LocalDateTime createdAt; // 요청 날짜
}
