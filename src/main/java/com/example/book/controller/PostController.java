package com.example.book.controller;

import com.example.book.domain.PostDetails;
import com.example.book.domain.PostHandling;
import com.example.book.domain.UserInfoDetails;
import com.example.book.entity.Message;
import com.example.book.entity.PostSharing;
import com.example.book.service.PostService;
import com.example.book.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final UserService userService;
    private final PostService postService;

    // Create post for admin
    @RequestMapping(path = "/admin/create-post", method = RequestMethod.POST)
    public String createPost(@ModelAttribute("post_create") @Valid PostHandling postHandling,
                             BindingResult result, RedirectAttributes redirectAttributes) {
        postService.createPost(postHandling, result, redirectAttributes);
        return "redirect:/admin/community";
    }

    // Create post for user
    @RequestMapping(path = "/user/create-post", method = RequestMethod.POST)
    public String addPost(@ModelAttribute("post_create") @Valid PostHandling postHandling,
                             BindingResult result, RedirectAttributes redirectAttributes) {
        postService.createPost(postHandling, result, redirectAttributes);
        return "redirect:/user/community";
    }

    // Edit post for admin
    @RequestMapping(path = "/admin/edit-post/{id}", method = RequestMethod.POST)
    public String editPost(@PathVariable("id") UUID post_id,
                           @RequestParam("title") String title,
                           @RequestParam("content_image") String content_image,
                           @RequestParam("content_text") String content_text,
                           RedirectAttributes redirectAttributes){
        PostHandling postHandling = new PostHandling(post_id,title,content_text,content_image);
        postService.configureUpdatedPostBeforeSaving(postHandling,redirectAttributes);
        return "redirect:/admin/community";
    }

    // Edit post for user
    @RequestMapping(path = "/user/edit-post/{id}", method = RequestMethod.POST)
    public String updatePost(@PathVariable("id") UUID post_id,
                           @RequestParam("title") String title,
                           @RequestParam("content_image") String content_image,
                           @RequestParam("content_text") String content_text,
                           RedirectAttributes redirectAttributes){
        PostHandling postHandling = new PostHandling(post_id,title,content_text,content_image);
        postService.configureUpdatedPostBeforeSaving(postHandling,redirectAttributes);
        return "redirect:/user/community";
    }

    // Delete post for admin
    @RequestMapping(path = "/admin/delete-post/{id}", method = RequestMethod.GET)
    public String deletePost(@PathVariable("id") UUID post_id){
        postService.deletePost(post_id);
        return "redirect:/admin/community";
    }

    // Delete post for user
    @RequestMapping(path = "/user/delete-post/{id}", method = RequestMethod.GET)
    public String removePost(@PathVariable("id") UUID post_id){
        postService.deletePost(post_id);
        return "redirect:/user/community";
    }

    // Share post for admin
    @RequestMapping(path = "/admin/share-post", method = RequestMethod.GET)
    public String sharePost(@RequestParam("member_id") UUID member_id, @RequestParam("post_id") UUID post_id, RedirectAttributes redirectAttributes){
        PostSharing postSharing = postService.sharePostToUser(post_id, member_id);
        redirectAttributes.addFlashAttribute("message","Post shared successfully.");
        return "redirect:/admin/community";
    }

    // Share post for user
    @RequestMapping(path = "/user/share-post", method = RequestMethod.GET)
    public String shareBlog(@RequestParam("member_id") UUID member_id, @RequestParam("post_id") UUID post_id, RedirectAttributes redirectAttributes){
        PostSharing postSharing = postService.sharePostToUser(post_id, member_id);
        redirectAttributes.addFlashAttribute("message","Post shared successfully.");
        return "redirect:/user/community";
    }

    // View shared post by other users for admin
    @RequestMapping(path = "/admin/view-shared-post", method = RequestMethod.GET)
    public String viewSharedPostByOther(Model model, RedirectAttributes redirectAttributes){
        userService.addUserAttributesToModel(model);
        postService.configureSharedPostByOtherUser(model, redirectAttributes);
        List<UserInfoDetails> friends = userService.getFriendListByUser(null);
        model.addAttribute("friends", friends);
        return "admin/view_sharedPost";
    }

    // View shared post by other users for user
    @RequestMapping(path = "/user/view-shared-post", method = RequestMethod.GET)
    public String visitSharedPostByOther(Model model, RedirectAttributes redirectAttributes){
        userService.addUserAttributesToModel(model);
        postService.configureSharedPostByOtherUser(model, redirectAttributes);
        List<UserInfoDetails> friends = userService.getFriendListByUser(null);
        model.addAttribute("friends", friends);
        return "user/view_sharedPost";
    }

    // Delete shared post for admin
    @RequestMapping(path = "/admin/delete-share/{id}", method = RequestMethod.GET)
    public String deleteSharedPost(@PathVariable("id") UUID shared_id){
        postService.deleteSharedPost(shared_id);
        return "redirect:/admin/view-shared-post";
    }

    // Delete shared post for admin
    @RequestMapping(path = "/user/delete-share/{id}", method = RequestMethod.GET)
    public String removeSharedPost(@PathVariable("id") UUID shared_id){
        postService.deleteSharedPost(shared_id);
        return "redirect:/user/view-shared-post";
    }
}
