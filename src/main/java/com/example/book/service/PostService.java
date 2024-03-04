package com.example.book.service;

import com.example.book.domain.CommentDetails;
import com.example.book.domain.PostDetails;
import com.example.book.domain.PostHandling;
import com.example.book.domain.UserInfoDetails;
import com.example.book.entity.*;
import com.example.book.repository.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostService {
    private final Validator validator;
    private final ModelMapper mapper;
    private final PostRepository postRepository;
    private final PostSharingRepository postSharingRepository;
    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final RateService rateService;
    private final CommentService commentService;

    public PostSharing sharePostToUser(UUID post_id, UUID user_id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).get();
        User receiver = userRepository.findUserById(user_id).orElseThrow();
        Post post = postRepository.findPostById(post_id).orElseThrow();
        PostSharing postSharing = new PostSharing(UUID.randomUUID(),current_user,receiver,post, LocalDateTime.now());
        // Add new user action to user history
        UserHistory userHistory = new UserHistory(current_user,LocalDateTime.now(),"share a post");
        userHistoryRepository.save(userHistory);
        return postSharingRepository.save(postSharing);
    }

    public void saveNewPost(PostHandling postHandling){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName()).get();
        Post post = mapper.map(postHandling, Post.class);
        if (postHandling.getContent_image().isEmpty()){
            post.setContent_image(null);
        }
        post.setId(UUID.randomUUID());
        post.setCreator(user);
        post.setTitle(postHandling.getTitle());
        post.setContent_text(postHandling.getContent_text());
        post.setCreated_time(LocalDateTime.now());
        postRepository.save(post);
        // Add new user action to user history
        UserHistory userHistory = new UserHistory(user,LocalDateTime.now(),"create a post");
        userHistoryRepository.save(userHistory);
    }

    public void configureUpdatedPostBeforeSaving(@Valid PostHandling postHandling, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).get();
        Set<ConstraintViolation<PostHandling>> violations = validator.validate(postHandling);
        if (!violations.isEmpty()){
            String message = "Error while updating post. Validation failed.";
            System.out.println(message);
            redirectAttributes.addFlashAttribute("message", message);
        }
        else {
            Post post = postRepository.findById(postHandling.getId()).orElseThrow();
            post.setTitle(postHandling.getTitle());
            post.setContent_text(postHandling.getContent_text());
            if (postHandling.getContent_image().isEmpty()){
                post.setContent_image(null);
            } else {
                post.setContent_image(postHandling.getContent_image());
            }
            post.setCreated_time(LocalDateTime.now());
            postRepository.save(post);
            // Add new user action to user history
            UserHistory userHistory = new UserHistory(current_user,LocalDateTime.now(),"update a post");
            userHistoryRepository.save(userHistory);
        }
    }

    public void deletePost(UUID post_id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).get();
        Post post = postRepository.findById(post_id).orElseThrow();
        postRepository.delete(post);
        // Add new user action to user history
        UserHistory userHistory = new UserHistory(current_user,LocalDateTime.now(),"remove a post");
        userHistoryRepository.save(userHistory);
    }

    public void configureCommunityPage(Model model, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message","");
        List<PostDetails> posts = getPosts();
        for(PostDetails post : posts){
            configureActionForEachPost(post, model);
        }
        model.addAttribute("post_create", new PostHandling());
        model.addAttribute("posts",posts);
    }

    public void configureSharedPostByOtherUser(Model model, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message","");
        List<PostDetails> shared_posts = getSharedPostByOtherUser();
        for(PostDetails post : shared_posts){
            configureActionForEachPost(post, model);
        }
        model.addAttribute("posts",shared_posts);
    }

    public void configureViewingSpecificPost(PostDetails postDetails, Model model){
        configureActionForEachPost(postDetails,model);
        model.addAttribute("post_create", new PostHandling());
    }

    public void configureActionForEachPost(PostDetails post, Model model){
        post.setPost_id(post.getId().toString());
        // Calculate star rating of a post
        float average_star = rateService.calculateAverageRateInPost(post);
        String star_text = String.format("%.1f", average_star).replace('.', ',');
        String[] starRatings = rateService.calculateStarRatings(average_star);
        String people_rates = rateService.countRateByPost(post.getId());
        // Get comments and their replies of a post
        List<CommentDetails> comments = commentService.viewCommentsInPost(post.getId());
        post.setComments(comments);
        // Add to model
        model.addAttribute("average_star_" + post.getPost_id(), star_text);
        model.addAttribute("people_rates_" + post.getPost_id(), people_rates);
        model.addAttribute("star_rating_" + post.getPost_id(), starRatings);
    }

    public PostDetails configurePostBeforeShowing(Post post){
        User creator = post.getCreator();
        PostDetails postDetails = mapper.map(post, PostDetails.class);
        // Format created_time for post
        LocalDateTime created_time = post.getCreated_time();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy 'at' hh:mm a");
        postDetails.setLast_updated(created_time.format(formatter));
        // Mapper for post creator image
        UserInfoDetails creator_detail = mapper.map(creator, UserInfoDetails.class);
        creator_detail.setRole_name(creator.getRole().getName());
        // Set creator image for post
        postDetails.setCreator_detail(creator_detail);
        return postDetails;
    }

    public void createPost(PostHandling postHandling, BindingResult result, RedirectAttributes redirectAttributes){
        String message;
        if (result.hasErrors()){
            message = "Error while creating new post. Validation failed.";
            System.out.println(message);
            redirectAttributes.addFlashAttribute("message", message);
        } else {
            saveNewPost(postHandling);
        }
    }

    public List<PostDetails> getPosts(){
        List<Post> posts = postRepository.findPostsOrderByTime();
        List<PostDetails> community_posts = new ArrayList<>();
        for (Post post : posts){
            PostDetails postDetails = configurePostBeforeShowing(post);
            community_posts.add(postDetails);
        }
        return community_posts;
    }

    public List<PostDetails> getSharedPostByOtherUser(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy 'at' hh:mm a");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).get();
        List<PostDetails> shared_posts = new ArrayList<>();
        List<PostSharing> postSharingList = postSharingRepository.findPostsBySharedToUserId(current_user.getId());

        for (PostSharing postSharing : postSharingList){
            User shared_by = postSharing.getSharedBy();
            Post post = postSharing.getPost();
            PostDetails postDetails = configurePostBeforeShowing(post);
            postDetails.setShared_post_id(postSharing.getId());
            postDetails.setShared_time(formatter.format(postSharing.getShared_time()));
            postDetails.setShared_user(mapper.map(shared_by, UserInfoDetails.class));
            shared_posts.add(postDetails);
        }
        return shared_posts.stream()
                .sorted(Comparator.comparing(PostDetails::getShared_time).reversed())
                .collect(Collectors.toList());
    }

    public void deleteSharedPost(UUID shared_id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).get();
        PostSharing existing_shared_post = postSharingRepository.findById(shared_id).orElseThrow();
        postSharingRepository.delete(existing_shared_post);
        // Add new user action to user history
        UserHistory userHistory = new UserHistory(current_user,LocalDateTime.now(),"delete their shared post");
        userHistoryRepository.save(userHistory);
    }
}
