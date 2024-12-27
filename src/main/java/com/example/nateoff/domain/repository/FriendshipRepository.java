package com.example.nateoff.domain.repository;


import com.example.nateoff.domain.dto.FriendRequestDto;
import com.example.nateoff.domain.entity.Friendship;
import com.example.nateoff.domain.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findAllByUserIdAndIsAcceptedTrue(Long userId);
    boolean existsByUserAndFriend(User user, User friend);
    // 친구 요청 중 수락된 목록 조회
    @Query("SELECT f.friend FROM Friendship f WHERE f.user.id = :userId AND f.isAccepted = true")
    List<User> findAcceptedFriends(@Param("userId") Long userId);

    // 친구 요청 대기 목록 조회
    @Query("SELECT new com.example.nateoff.domain.dto.FriendRequestDto(f.id, f.user.id, f.friend.id, f.createdAt) " +
            "FROM Friendship f WHERE f.friend.id = :userId AND f.isAccepted = false")
    List<FriendRequestDto> findPendingRequests(@Param("userId") Long userId);
}
