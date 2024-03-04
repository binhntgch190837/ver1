package com.example.book.repository;

import com.example.book.entity.PostSharing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostSharingRepository extends JpaRepository<PostSharing, UUID> {
    @Query("SELECT ps FROM PostSharing ps WHERE ps.sharedTo.id = :sharedToUserId")
    List<PostSharing> findPostsBySharedToUserId(@Param("sharedToUserId") UUID sharedToUserId);
}
