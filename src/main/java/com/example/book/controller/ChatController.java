package com.example.book.controller;

import com.example.book.domain.MessageDetails;
import com.example.book.domain.UserInfoDetails;
import com.example.book.entity.Chat;
import com.example.book.entity.Message;
import com.example.book.entity.User;
import com.example.book.service.ChatService;
import com.example.book.service.PostService;
import com.example.book.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final UserService userService;
    private final ChatService chatService;

    // Messenger for admin
    @RequestMapping(value = "/admin/message-index", method = RequestMethod.GET)
    public String chatList(Model model,@RequestParam(required = false) String username){
        userService.addUserAttributesToModel(model);
        chatService.updateMessageIndexData(model, username);
        return "admin/message_index";
    }

    //Messenger for user
    @RequestMapping(value = "/user/message-index", method = RequestMethod.GET)
    public String messageList(Model model, @RequestParam(required = false) String username){
        userService.addUserAttributesToModel(model);
        chatService.updateMessageIndexData(model, username);
        return "user/chat_index";
    }

    // Direct message for admin
    @RequestMapping(value = "/admin/message-box/{id}", method = RequestMethod.GET)
    public String messageBox(Model model, @PathVariable(value = "id") UUID member_id,
                             @RequestParam(required = false) String username,
                             RedirectAttributes redirectAttributes){
        userService.addUserAttributesToModel(model);
        chatService.openDirectMessageForUser(model, member_id, username, redirectAttributes);
        return "admin/inbox";
    }

    // Direct message for user
    @RequestMapping(value = "/user/direct-message/{id}")
    public String directMessenger(Model model, @PathVariable(value = "id") UUID member_id,
                                  @RequestParam(required = false) String username,
                                  RedirectAttributes redirectAttributes){
        userService.addUserAttributesToModel(model);
        chatService.openDirectMessageForUser(model, member_id, username, redirectAttributes);
        return "user/inbox";
    }

    // Start a new chat for admin with one other member
    @RequestMapping(value = "/admin/open-chat/{id}", method = RequestMethod.GET)
    public String openMessage(@PathVariable("id") UUID user_id){
        Chat chat = chatService.startAChat(user_id);
        return "redirect:/admin/message-box/" + user_id;
    }

    // Start a new chat for user with one other member
    @RequestMapping(value = "/user/open-chat/{id}", method = RequestMethod.GET)
    public String openChat(@PathVariable("id") UUID user_id){
        Chat chat = chatService.startAChat(user_id);
        return "redirect:/user/direct-message/" + user_id;
    }

    // Send message for admin
    @RequestMapping(value = "/admin/send-message/{id}", method = RequestMethod.POST)
    public String sendMessage(@PathVariable("id") UUID member_uid,
                              @RequestParam("message_text") String message_text,
                              RedirectAttributes redirectAttributes){
        if (message_text.isEmpty()){
            redirectAttributes.addFlashAttribute("message","This field cannot be empty.");
        }
        else {
            Message message = chatService.saveMessage(member_uid, message_text);
        }
        return "redirect:/admin/message-box/" + member_uid;
    }

    // Send message for user
    @RequestMapping(value = "/user/send-message/{id}", method = RequestMethod.POST)
    public String tweetMessage(@PathVariable("id") UUID member_uid,
                              @RequestParam("message_text") String message_text,
                              RedirectAttributes redirectAttributes){
        if (message_text.isEmpty()){
            redirectAttributes.addFlashAttribute("message","This field cannot be empty.");
        }
        else {
            Message message = chatService.saveMessage(member_uid, message_text);
        }
        return "redirect:/user/direct-message/" + member_uid;
    }

    // Delete chat for admin
    @RequestMapping(value = "/admin/delete-chat/{id}", method = RequestMethod.GET)
    public String deleteChat(@PathVariable("id") UUID chat_id){
        chatService.deleteChat(chat_id);
        return "redirect:/admin/message-index";
    }

    // Delete chat for user
    @RequestMapping(value = "/user/delete-chat/{id}", method = RequestMethod.GET)
    public String removeChat(@PathVariable("id") UUID chat_id){
        chatService.deleteChat(chat_id);
        return "redirect:/user/message-index";
    }

    // Edit message for admin
    @RequestMapping(value = "/admin/edit-message/{id}", method = RequestMethod.POST)
    public String editMessage(@PathVariable("id") UUID message_id, @RequestParam("message_text") String message_text){
        System.out.println("Edit message id: "+ message_id);
        Message message = chatService.updateMessage(message_id, message_text);
        return "redirect:/admin/message-index";
    }

    // Edit message for user
    @RequestMapping(value = "/user/edit-message/{id}", method = RequestMethod.POST)
    public String updateMessage(@PathVariable("id") UUID message_id, @RequestParam("message_text") String message_text){
        System.out.println("Edit message id: "+ message_id);
        Message message = chatService.updateMessage(message_id, message_text);
        return "redirect:/user/message-index";
    }

    @RequestMapping(value = "/user/delete-message/{id}", method = RequestMethod.GET)
    public String removeMessage(@PathVariable("id") UUID message_id){
        System.out.println("Delete message id: " + message_id);
        chatService.deleteMessage(message_id);
        return "redirect:/user/message-index";
    }

    @RequestMapping(value = "/admin/delete-message/{id}", method = RequestMethod.GET)
    public String deleteMessage(@PathVariable("id") UUID message_id){
        System.out.println("Delete message id: " + message_id);
        chatService.deleteMessage(message_id);
        return "redirect:/admin/message-index";
    }
}
