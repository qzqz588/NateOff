package com.example.nateoff.domain.repository;

import com.example.nateoff.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.username = :username")
    boolean existsByUsername(String username);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.phone = :phone AND u.email = :email")
    boolean findByPhoneAndEmail(String phone,String email);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    User findByUsernameAndEmail(String username, String email);
    Optional<User> findByUsername(String username);
}
