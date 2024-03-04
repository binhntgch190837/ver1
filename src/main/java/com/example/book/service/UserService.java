package com.example.book.service;

import com.example.book.domain.*;
import com.example.book.entity.*;
import com.example.book.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserHistoryRepository userHistoryRepository;

    public Set<SimpleGrantedAuthority> getRole(User user) {
        Role role = user.getRole();
        return Collections.singleton(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.of(userRepository.findUserByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username " + username + " not found!")));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.get().getEmail(),
                user.get().getPassword(), getRole(user.get()));
        return userDetails;
    }


    // Save user to database after registration
    public void saveRegisteredUser(UserRegister userRegister){
        User user = mapper.map(userRegister, User.class);
        user.setId(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        user.setRole(roleRepository.findRoleByName("user"));
        user.setStatus("Enabled");
        user.setLast_updated(LocalDateTime.now());
        userRepository.save(user);
        Favorite favorite = new Favorite(user);
        favorite.setId(UUID.randomUUID());
        favoriteRepository.save(favorite);
    }

    // Save user to database after added by admin
    public void saveAddUser(UserHandling userHandling){
        User user = mapper.map(userHandling, User.class);
        user.setId(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(userHandling.getPassword()));
        user.setRole(roleRepository.findRoleByName(userHandling.getInput_role()));
        user.setLast_updated(LocalDateTime.now());
        userRepository.save(user);
        Favorite favorite = new Favorite(user);
        favorite.setId(UUID.randomUUID());
        favoriteRepository.save(favorite);
    }

    public void saveUpdatedUser(UserHandling userHandling){
        Optional<User> existed_user = userRepository.findById(userHandling.getId());
        if(existed_user.isPresent()){
            User updated_user = mapper.map(userHandling, User.class);
            updated_user.setId(userHandling.getId());
            updated_user.setRole(roleRepository.findRoleByName(userHandling.getInput_role()));
            updated_user.setLast_updated(LocalDateTime.now());
            updated_user.setPassword(passwordEncoder.encode(userHandling.getPassword()));
            userRepository.save(updated_user);
        }
    }

    @Transactional
    public void deleteUser(UUID user_id) {
        Optional<User> existingUser = userRepository.findById(user_id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            for (User system_user : userRepository.findAll()){
                userRepository.deleteFriendshipRelationship(user_id, system_user.getId());
            }
            userRepository.delete(user);
        }
    }

    public void configureUserBeforeEdit(UUID user_id, Model model){
        Optional<User> existing_user = Optional.of(userRepository.findById(user_id).orElseThrow());
        UserHandling userHandling = mapper.map(existing_user.get(), UserHandling.class);
        model.addAttribute("edit_user", userHandling);
    }

    // Configure user data before showing in user index
    public List<UserInfoDetails> configureUserInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).get();
        List<User> users = userRepository.findAll();
        users.remove(current_user);
        List<UserInfoDetails> userInfoDetailsList = new ArrayList<>();
        for (User user : users){
            UserInfoDetails userInfoDetails = mapper.map(user, UserInfoDetails.class);
            LocalDateTime last_updated = user.getLast_updated();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy 'at' hh:mm a");
            userInfoDetails.setLast_updated(last_updated.format(formatter));
            userInfoDetailsList.add(userInfoDetails);
        }
        return userInfoDetailsList;
    }

    public void viewUserPersonalInfo(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).get();
        UserBasicInfo userBasicInfo = mapper.map(current_user, UserBasicInfo.class);
        model.addAttribute("user_info", userBasicInfo);
    }

    // Get recommended users who share the same favorite books as logged-in user
    public List<UserInfoDetails> getUserWhoShareTheSameFavorite(){
        List<User> shared_users = new ArrayList<>();
        List<UserInfoDetails> shared_user_detailsList = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).get();
        List<User> system_users = userRepository.findNonFriendUsersByUserIdAndLoggedInUserId(current_user.getId());
        // Remove admin user
        system_users.removeIf(user -> Objects.equals(user.getRole().getName(), "admin"));
        // Get other user who share the same favorite books
        for (User user : system_users){
            for (Book book : current_user.getFavorites().getBooks()){
                if (user.getFavorites().getBooks().contains(book)){
                    shared_users.add(user);
                }
            }
        }
        for (User user : shared_users){
            UserInfoDetails shared_user_details = mapper.map(user, UserInfoDetails.class);
            shared_user_details.setRole_name(user.getRole().getName());
            shared_user_detailsList.add(shared_user_details);
        }
        return shared_user_detailsList;
    }

    public void configureUsersIncludeFriends(Model model, String username, String friend_name){
        List<UserInfoDetails> friends = getFriendListByUser(friend_name);
        List<UserInfoDetails> other_users = getOtherUsers(username);
        model.addAttribute("friends", friends);
        model.addAttribute("users", other_users);
    }

    //Show friend list of a current logged user
    public List<UserInfoDetails> getFriendListByUser(String friend_name){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName()).get();
        List<User> friends;
        if (friend_name != null && !friend_name.isEmpty()){
            friends = userRepository.searchFriendListByUser(user.getId(),friend_name);
        }
        else {
            friends = userRepository.findFriendListByUser(user.getId());
        }
        List<UserInfoDetails> friend_details = new ArrayList<>();
        for (User friend : friends){
            UserInfoDetails friend_detail = mapper.map(friend, UserInfoDetails.class);
            friend_detail.setRole_name(friend.getRole().getName());
            friend_details.add(friend_detail);
        }
        return friend_details;
    }

    // Show other users of the system who are not friend of the logged user
    public List<UserInfoDetails> getOtherUsers(String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).orElseThrow();
        List<User> users;
        if (username != null && !username.isEmpty()){
            users = userRepository.searchNonFriendUsersByUserIdAndLoggedInUserId(current_user.getId(), username);
        }
        else{
            users = userRepository.findNonFriendUsersByUserIdAndLoggedInUserId(current_user.getId());
        }
        List<UserInfoDetails> system_users = new ArrayList<>();
        for (User user : users){
            UserInfoDetails userInfoDetails = mapper.map(user, UserInfoDetails.class);
            userInfoDetails.setRole_name(user.getRole().getName());
            system_users.add(userInfoDetails);
        }
        return system_users;
    }

    public User addFriend(UUID friend_id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).orElseThrow();
        User friend = userRepository.findUserById(friend_id).orElseThrow();
        current_user.getFriends().add(friend);
        // Add new user action to user history
        UserHistory userHistory = new UserHistory(current_user,LocalDateTime.now(),"add a new friend");
        userHistoryRepository.save(userHistory);
        return userRepository.save(current_user);
    }

    public User removeFriend(UUID friend_id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).orElseThrow();
        User friend = userRepository.findUserById(friend_id).orElseThrow();
        current_user.getFriends().remove(friend);
        // Add new user action to user history
        UserHistory userHistory = new UserHistory(current_user,LocalDateTime.now(),"remove a friend");
        userHistoryRepository.save(userHistory);
        return userRepository.save(current_user);
    }

    // Get Action history of a specific user
    public List<UserActionHistory> getUserActionHistory(UUID user_id){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy 'at' hh:mm a");
        User user = userRepository.findUserById(user_id).orElseThrow();
        List<UserActionHistory> userActionHistories = new ArrayList<>();
        List<UserHistory> userHistories = userHistoryRepository.getHistoryByUser(user);
        for (UserHistory userHistory : userHistories){
            UserActionHistory userActionHistory = mapper.map(userHistory, UserActionHistory.class);
            userActionHistory.setAction_detail(user.getUsername() + " " + userHistory.getAction_detail());
            userActionHistory.setAction_time(formatter.format(userHistory.getTrack_time()));
            userActionHistories.add(userActionHistory);
        }
        return userActionHistories;
    }

    public void saveUpdatedUserInfo(UserBasicInfo userBasicInfo){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> existed_user = userRepository.findUserById(userBasicInfo.getId());
        if(existed_user.isPresent()){
            User updated_user = existed_user.get();
            updated_user.setEmail(userBasicInfo.getEmail());
            updated_user.setPhone_number(userBasicInfo.getPhone_number());
            updated_user.setImage_url(userBasicInfo.getImage_url());
            updated_user.setLast_updated(LocalDateTime.now());
            updated_user.setPassword(passwordEncoder.encode(userBasicInfo.getPassword()));
            userRepository.save(updated_user);
            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(userBasicInfo.getEmail(), auth.getCredentials(), auth.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }

    public List<String> getUserStatus(){
        List<String> status = new ArrayList<>();
        status.add("Enabled");
        status.add("Disabled");
        return status;
    }

    public List<String> getUserRole(){
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        roles.add("user");
        return roles;
    }

    public void addUserAttributesToModel(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName()).get();
        String role = user.getRole().getName();
        model.addAttribute("user_detail", user);
        model.addAttribute("role", role);
    }

    public void updateModel(Model model){
        addUserAttributesToModel(model);
        model.addAttribute("status",getUserStatus());
        model.addAttribute("roles", getUserRole());
    }
}
