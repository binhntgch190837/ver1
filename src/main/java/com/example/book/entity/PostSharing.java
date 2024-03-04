package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "post_sharing")
@Getter
@RequiredArgsConstructor
public class PostSharing {
    @Id
    private UUID id;

    @Column
    private LocalDateTime shared_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User sharedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_to_user_id")
    private User sharedTo;

    public PostSharing(UUID id, User sender, User receiver, Post post, LocalDateTime shared_time) {
        this.id = id;
        this.sharedBy = sender;
        this.sharedTo = receiver;
        this.post = post;
        this.shared_time = shared_time;
    }
}
