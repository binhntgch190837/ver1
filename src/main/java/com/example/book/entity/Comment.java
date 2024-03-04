package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String text;

    @Column
    private LocalDateTime created_time;

    // A comment can have one or many replies of itself
    // So the comment that contain its replies is called a parent comment
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> replies;

    // If a comment is a top-level comment, the parent_id will be null
    @ManyToOne
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "user_comment", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_comment", nullable = false)
    private Post post;

    public Comment(User user, Post post){
        this.user = user;
        this.post = post;
    }

}
