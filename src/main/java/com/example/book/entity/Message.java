package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@Setter
@RequiredArgsConstructor
public class Message {
    @Id
    private UUID id;

    @Column
    private String text;

    @Column
    private LocalDateTime created_time;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    public Message(User sender, String text, LocalDateTime created_time){
        this.sender = sender;
        this.text = text;
        this.created_time = created_time;
    }

    public Message(User sender, Chat chat, LocalDateTime created_time){
        this.sender = sender;
        this.chat = chat;
        this.created_time = created_time;
    }
}
