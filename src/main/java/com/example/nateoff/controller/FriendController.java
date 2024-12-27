package com.example.nateoff.controller;

import com.example.nateoff.domain.dto.FriendRequestDto;
import com.example.nateoff.domain.entity.User;
import com.example.nateoff.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 유저 검색
    @GetMapping("/search")
    public ResponseEntity<User> findUserByUsername(@RequestParam String username) {
        User user = friendService.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    // 친구 요청 보내기
    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
        friendService.sendFriendRequest(friendRequestDto);
        return ResponseEntity.ok("친구 요청이 전송되었습니다.");
    }

    // 친구 요청 수락
    @PutMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestParam Long requestId) {
        friendService.acceptFriendRequest(requestId);
        return ResponseEntity.ok("친구 요청이 수락되었습니다.");
    }

    // 친구 요청 거절
    @DeleteMapping("/decline")
    public ResponseEntity<String> declineFriendRequest(@RequestParam Long requestId) {
        friendService.declineFriendRequest(requestId);
        return ResponseEntity.ok("친구 요청이 거절되었습니다.");
    }

    // 친구 목록 조회
    @GetMapping
    public ResponseEntity<List<User>> getFriendList(@RequestParam Long userId) {
        List<User> friends = friendService.getFriendList(userId);
        return ResponseEntity.ok(friends);
    }
}
