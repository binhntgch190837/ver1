package com.example.book.controller;

import com.example.book.domain.UserRegister;
import com.example.book.entity.User;
import com.example.book.repository.UserRepository;
import com.example.book.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserRepository userRepository;
    private final UserService userService;
    @GetMapping(value = "/")
    public String homeIndex(){
        return "home";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/login-success")
    public String loginSuccess(Authentication authentication){
        if(authentication != null){
            final UserDetails userDetails = userService
                    .loadUserByUsername(authentication.getName());
            System.out.println("Account logged in: "+userDetails);

            String role = authentication.getAuthorities().toString();
            System.out.println("User role: "+role);

            if(role.contains("admin")){
                return "redirect:/admin/dashboard";
            }
            else if (role.contains("user")){
                User user = userRepository.findUserByEmail(authentication.getName()).orElseThrow();
                if (user.getStatus().contains("Disabled")){
                    return "redirect:/user/access-denied";
                }
                else {
                    return "redirect:/user/home-page";
                }
            }
            else{
                return "error_page";
            }
        } else {
            return "error_page";
        }
    }

    @GetMapping(value = "/server-error")
    public String errorPage(){
        return "error_page";
    }

    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("user",new UserRegister());
        return "register";
    }

    @PostMapping(value = "/register")
    public String registerForm(@ModelAttribute("user") @Valid UserRegister userRegister, BindingResult result,
                               @RequestParam(value = "termCheck",required = false) Boolean termCheck){
        if(result.hasErrors()){
            return "register";
        }
        else if (userRepository.findUserByName(userRegister.getUsername()).isPresent()) {
            result.rejectValue("username",null,"Username already exists.");
            return "register";
        }
        else if (userRepository.findUserByEmail(userRegister.getEmail()).isPresent()) {
            result.rejectValue("email",null,"Email already exists.");
            return "register";
        }
        else if (userRepository.findUserByPhoneNumber(userRegister.getPhone_number()).isPresent()) {
            result.rejectValue("phone_number",null,"Phone number already exists.");
            return "register";
        }
        else if(!Objects.equals(userRegister.getPassword(), userRegister.getConfirm_password())){
            result.rejectValue("confirm_password",null,"Confirm password does not match.");
            return "register";
        }
        else if(termCheck == null || !termCheck){
            return "register";
        }
        else {
            userService.saveRegisteredUser(userRegister);
            return "home";
        }
    }

    @GetMapping(value = "/logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}
