package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    private UUID id;

    @Column
    private String username;

    @Column
    private String image_url;

    @Column
    private String email;

    @Column
    private String phone_number;

    @Column
    private String status;

    @Column
    private LocalDateTime last_updated;

    @Column
    private String password;

    @ManyToOne
    @JoinColumn(name = "user_role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Rate> rates;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Favorite favorites;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // One user can have many chats with different users
    // One user can have only one chat to one specific user
    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private List<Chat> chat_user1;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    private List<Chat> chats_user2;

    // An user can create many posts
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserHistory> user_histories;

    // An user can share many posts to other users
    @OneToMany(mappedBy = "sharedBy", cascade = CascadeType.ALL)
    private List<PostSharing> sharedPosts = new ArrayList<>();

    // An user can have many friends
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends;
}