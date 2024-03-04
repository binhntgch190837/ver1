package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@RequiredArgsConstructor
public class Post {
    @Id
    private UUID id;

    @Column
    private String title;

    @Column
    private String content_image;

    @Column
    private String content_text;

    @Column
    private LocalDateTime created_time;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Rate> rates;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_post", referencedColumnName = "id")
    private User creator;

    // A post can be shared by many users
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostSharing> sharedBy = new ArrayList<>();
}
