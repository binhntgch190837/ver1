package com.example.book.service;

import com.example.book.domain.CommentDetails;
import com.example.book.domain.UserInfoDetails;
import com.example.book.entity.Comment;
import com.example.book.entity.Post;
import com.example.book.entity.User;
import com.example.book.repository.CommentRepository;
import com.example.book.repository.PostRepository;
import com.example.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper mapper;

    public Comment writeCommentForPost(UUID post_id, String comment_text){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName()).get();
        Optional<Post> post = postRepository.findById(post_id);
        Comment comment = new Comment(user,post.get());
        comment.setText(comment_text);
        comment.setCreated_time(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Comment updateCommentForPost(Integer comment_id, String comment_text){
        Comment comment = commentRepository.findById(comment_id).orElseThrow();
        comment.setText(comment_text);
        comment.setCreated_time(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Comment updateReplyForComment(Integer reply_id, String reply_text){
        Optional<Comment> existing_reply = commentRepository.findCommentByID(reply_id);
        if (existing_reply.isPresent()){
            Comment reply = existing_reply.get();
            System.out.println(reply.getId());
            reply.setText(reply_text);
            reply.setCreated_time(LocalDateTime.now());
            return commentRepository.save(reply);
        }
        else {
            System.out.println("Reply not found");
            return null;
        }
    }

    public void deleteComment(Integer comment_id){
        Comment comment = commentRepository.findById(comment_id).orElseThrow();
        commentRepository.delete(comment);
    }

    public void deleteReply(Integer reply_id){
        Optional<Comment> reply = commentRepository.findCommentByID(reply_id);
        if (reply.isPresent()){
            commentRepository.delete(reply.get());
        }
        else {
            System.out.println("Reply not found");
        }
    }

    public Comment saveReply(Integer comment_id, String reply){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName()).get();
        Optional<Comment> comment = commentRepository.findCommentByID(comment_id);
        if (comment.isPresent()){
            Comment reply_comment = new Comment();
            reply_comment.setUser(user);
            reply_comment.setParent(comment.get());
            reply_comment.setText(reply);
            reply_comment.setCreated_time(LocalDateTime.now());
            return commentRepository.save(reply_comment);
        }
        else {
            System.out.println("The current comment you are reply to are not exist.");
            return null;
        }
    }

    // Get top-level comments for a post
    public List<CommentDetails> viewCommentsInPost(UUID post_id){
        List<Comment> comments = commentRepository.getCommentsForPost(post_id);
        List<CommentDetails> commentDTOList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy 'at' hh:mm a");
        for (Comment comment : comments){
            CommentDetails commentDetails = configureCommentCreatorImage(comment);
            commentDetails.setReplies(viewRepliesInComment(comment));
            commentDetails.setUpdated_time(comment.getCreated_time().format(formatter));
            commentDTOList.add(commentDetails);
        }
        return commentDTOList;
    }

    public List<CommentDetails> viewRepliesInComment(Comment comment){
        List<Comment> replies = commentRepository.getRepliesByComment(comment);
        List<CommentDetails> replyDetailList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy 'at' hh:mm a");
        for (Comment reply : replies){
            CommentDetails replyDetails = configureCommentCreatorImage(reply);
            replyDetails.setUpdated_time(reply.getCreated_time().format(formatter));
            replyDetailList.add(replyDetails);
        }
        return replyDetailList;
    }

    private CommentDetails configureCommentCreatorImage(Comment comment){
        User creator = comment.getUser();
        CommentDetails commentDetails = mapper.map(comment, CommentDetails.class);
        // Mapper for comment creator image
        UserInfoDetails creator_detail = mapper.map(creator, UserInfoDetails.class);
        // Set creator image for post
        commentDetails.setCreator(creator_detail);
        return commentDetails;
    }
}
