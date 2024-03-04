package com.example.book.repository;

import com.example.book.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query("SELECT p from Post p ORDER BY p.created_time DESC")
    List<Post> findPostsOrderByTime();

    @Query("SELECT p from Post p WHERE p.id =:id")
    Optional<Post> findPostById(@Param("id") UUID post_id);
}
