package com.example.book.repository;

import com.example.book.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    @Query("SELECT c FROM Chat c WHERE (c.user1.id = :user1_id AND c.user2.id = :user2_id) " +
            "OR (c.user1.id = :user2_id AND c.user2.id = :user1_id)")
    Optional<Chat> findChatByUsers(@RequestParam("user1_id") UUID user1_id, @RequestParam("user2_id") UUID user2_id);

    @Query("SELECT c FROM Chat c WHERE c.last_access = (SELECT MAX(c2.last_access) FROM Chat c2)")
    Chat getLastAccessedChat();
}
