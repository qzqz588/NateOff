package com.example.nateoff.service;

import com.example.nateoff.domain.dto.FriendRequestDto;
import com.example.nateoff.domain.entity.Friendship;
import com.example.nateoff.domain.entity.User;
import com.example.nateoff.domain.repository.FriendshipRepository;
import com.example.nateoff.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    // 유저 검색
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    }

    // 친구 요청 보내기
    public void sendFriendRequest(FriendRequestDto friendRequestDto) {
        User user = userRepository.findById(friendRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("요청 보낸 유저를 찾을 수 없습니다."));
        User friend = userRepository.findById(friendRequestDto.getFriendId())
                .orElseThrow(() -> new RuntimeException("요청 받은 유저를 찾을 수 없습니다."));

        if (friendshipRepository.existsByUserAndFriend(user, friend)) {
            throw new RuntimeException("이미 친구 요청을 보냈거나 친구입니다.");
        }

        Friendship friendship = Friendship.builder()
                .user(user)
                .friend(friend)
                .isAccepted(false)
                .createdAt(LocalDateTime.now())
                .build();

        friendshipRepository.save(friendship);
    }

    // 친구 요청 수락
    public void acceptFriendRequest(Long requestId) {
        Friendship friendship = friendshipRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("친구 요청을 찾을 수 없습니다."));
        friendship.setAccepted(true);
        friendshipRepository.save(friendship);
    }

    // 친구 요청 거절
    public void declineFriendRequest(Long requestId) {
        Friendship friendship = friendshipRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("친구 요청을 찾을 수 없습니다."));
        friendshipRepository.delete(friendship);
    }

    // 친구 목록 조회
    public List<User> getFriendList(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllByUserIdAndIsAcceptedTrue(userId);
        return friendships.stream()
                .map(Friendship::getFriend)
                .collect(Collectors.toList());
    }
}
