package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chats")
@Getter
@Setter
@RequiredArgsConstructor
public class Chat {
    @Id
    private UUID id;

    @Column
    private LocalDateTime created_time;

    @Column
    private LocalDateTime last_access;

    // One user can have many chats with different users
    // One user can have only one chat to one specific user
    @ManyToOne
    @JoinColumn(name = "user1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2", nullable = false)
    private User user2;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    public Chat(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
    }
}
