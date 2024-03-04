package com.example.book.repository;

import com.example.book.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c from Comment c " +
            "WHERE (c.post.id =:post_id) " +
            "AND (c.parent IS NULL) " +
            "ORDER BY c.created_time ASC")
    List<Comment> getCommentsForPost(@Param("post_id") UUID post_id);

    @Query("SELECT c from Comment c WHERE c.parent =:parent")
    List<Comment> getRepliesByComment(@Param("parent") Comment comment);

    @Query("SELECT c from Comment c WHERE c.id =:id")
    Optional<Comment> findCommentByID(@Param("id") Integer id);
}