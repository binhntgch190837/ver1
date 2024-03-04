package com.example.book.repository;

import com.example.book.entity.Chat;
import com.example.book.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE m.id = :message_id")
    Optional<Message> findMessageByID(@RequestParam("message_id") UUID message_id);

    @Query("SELECT m from Message m WHERE m.chat =:chat ORDER BY m.created_time ASC")
    List<Message> getMessageInChat(@RequestParam("chat") Chat chat);

    @Query("SELECT m FROM Message m WHERE m.chat = :chat AND m.created_time = (SELECT MAX(m2.created_time) FROM Message m2 WHERE m2.chat = :chat)")
    Message getLatestMessageInChat(@RequestParam("chat") Chat chat);

}
