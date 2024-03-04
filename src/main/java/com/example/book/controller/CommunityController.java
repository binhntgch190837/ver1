package com.example.book.controller;

import com.example.book.domain.CommentDetails;
import com.example.book.domain.PostDetails;
import com.example.book.domain.PostHandling;
import com.example.book.domain.UserInfoDetails;
import com.example.book.entity.Comment;
import com.example.book.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CommunityController {
    private final UserService userService;
    private final PostService postService;
    private final RateService rateService;
    private final CommentService commentService;
    private final AdvertisementService advertisementService;

    // Community page for admin
    @RequestMapping(value = "/admin/community", method = RequestMethod.GET)
    public String communityPage(Model model, RedirectAttributes redirectAttributes){
        userService.addUserAttributesToModel(model);
        postService.configureCommunityPage(model, redirectAttributes);
        List<UserInfoDetails> friends = userService.getFriendListByUser(null);
        model.addAttribute("friends", friends);
        return "admin/community";
    }

    // Community page for user
    @RequestMapping(value = "/user/community", method = RequestMethod.GET)
    public String socialPage(Model model, RedirectAttributes redirectAttributes){
        userService.addUserAttributesToModel(model);
        advertisementService.getEnabledAdvertisements(model);
        postService.configureCommunityPage(model, redirectAttributes);
        List<UserInfoDetails> friends = userService.getFriendListByUser(null);
        model.addAttribute("friends", friends);
        return "user/community";
    }

    // Star rating for admin
    @RequestMapping(value = "/admin/provide-rating/{id}", method = RequestMethod.POST)
    public String provideRating(@PathVariable("id") UUID post_id, @RequestParam("star") int star){
        rateService.updateRatingForPost(post_id,star);
        return "redirect:/admin/community";
    }

    // Star rating for user
    @RequestMapping(value = "/user/provide-rating/{id}", method = RequestMethod.POST)
    public String starRating(@PathVariable("id") UUID post_id, @RequestParam("star") int star){
        rateService.updateRatingForPost(post_id,star);
        return "redirect:/user/community";
    }

    // Write comment for admin
    @RequestMapping(value = "/admin/write-comment/{id}", method = RequestMethod.POST)
    public String writeComment(@PathVariable("id") UUID post_id, @RequestParam("comment") String comment_text){
        Comment comment = commentService.writeCommentForPost(post_id, comment_text);
        return "redirect:/admin/community";
    }

    // Write comment for user
    @RequestMapping(value = "/user/write-comment/{id}", method = RequestMethod.POST)
    public String postComment(@PathVariable("id") UUID post_id, @RequestParam("comment") String comment_text){
        Comment comment = commentService.writeCommentForPost(post_id, comment_text);
        return "redirect:/user/community";
    }

    // Edit comment for admin
    @RequestMapping(value = "/admin/edit-comment/{id}", method = RequestMethod.POST)
    public String editComment(@PathVariable("id") Integer comment_id, @RequestParam("comment") String comment_text){
        Comment updated_comment = commentService.updateCommentForPost(comment_id,comment_text);
        return "redirect:/admin/community";
    }

    // Edit comment for user
    @RequestMapping(value = "/user/edit-comment/{id}", method = RequestMethod.POST)
    public String updateComment(@PathVariable("id") Integer comment_id, @RequestParam("comment") String comment_text){
        Comment updated_comment = commentService.updateCommentForPost(comment_id,comment_text);
        return "redirect:/user/community";
    }

    // Delete comment for admin
    @RequestMapping(value = "/admin/delete-comment/{id}", method = RequestMethod.GET)
    public String deleteComment(@PathVariable("id") Integer comment_id){
        commentService.deleteComment(comment_id);
        return "redirect:/admin/community";
    }

    // Delete comment for user
    @RequestMapping(value = "/user/delete-comment/{id}", method = RequestMethod.GET)
    public String removeComment(@PathVariable("id") Integer comment_id){
        commentService.deleteComment(comment_id);
        return "redirect:/user/community";
    }

    // Reply comment for admin
    @RequestMapping(value = "/admin/reply/{id}", method = RequestMethod.POST)
    public String replyComment(@PathVariable("id") Integer comment_id, @RequestParam("reply") String reply){
        Comment reply_comment = commentService.saveReply(comment_id, reply);
        return "redirect:/admin/community";
    }

    // Reply comment for user
    @RequestMapping(value = "/user/reply/{id}", method = RequestMethod.POST)
    public String answerComment(@PathVariable("id") Integer comment_id, @RequestParam("reply") String reply){
        Comment reply_comment = commentService.saveReply(comment_id, reply);
        return "redirect:/user/community";
    }

    // Edit reply for admin
    @RequestMapping(value = "/admin/edit-reply/{id}", method = RequestMethod.POST)
    public String editReply(@PathVariable("id") Integer reply_id, @RequestParam("reply") String reply_text){
        Comment updated_reply = commentService.updateReplyForComment(reply_id, reply_text);
        return "redirect:/admin/community";
    }

    // Edit reply for user
    @RequestMapping(value = "/user/edit-reply/{id}", method = RequestMethod.POST)
    public String updateReply(@PathVariable("id") Integer reply_id, @RequestParam("reply") String reply_text){
        Comment updated_reply = commentService.updateReplyForComment(reply_id, reply_text);
        return "redirect:/user/community";
    }

    // Delete reply for admin
    @RequestMapping(value = "/admin/delete-reply/{id}", method = RequestMethod.GET)
    public String deleteReply(@PathVariable("id") Integer reply_id){
        commentService.deleteReply(reply_id);
        return "redirect:/admin/community";
    }

    // Delete reply for user
    @RequestMapping(value = "/user/delete-reply/{id}", method = RequestMethod.GET)
    public String removeReply(@PathVariable("id") Integer reply_id){
        commentService.deleteReply(reply_id);
        return "redirect:/user/community";
    }
}
