package com.blog.service.impl;

import com.blog.dto.UserDto;
import com.blog.entity.Post;
import com.blog.entity.Role;
import com.blog.entity.User;
import com.blog.repository.UserRepository;
import com.blog.service.PostService;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Value("${file.path}")
    private String path;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PostService postService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User registration(UserDto userDto) {

        User user = new User();
        user.setNickName(userDto.getLogin());
        user.setPassword(encoder.encode(userDto.getPassword()));
        if (userDto.getLogin().equals("admin")) user.setRole(Role.ADMIN);
        else user.setRole(Role.USER);
        user.setFirstName(userDto.getFirstName());
        user.setSecondName(userDto.getSecondName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPhotoUrl("6a4ca816-5109-43aa-9d46-066b6c781754.png");
        return save(user);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public Boolean registrationValidation(UserDto userDto) {
        if (!userDto.getEmail().contains("@")) return false;
        if (!userDto.getPassword().equals(userDto.getPasswordRepeat())) return false;
        // if (userDto.getPhone().length() != 10) return false;
        if (repository.countByEmail(userDto.getEmail()) > 0) return false;
        if (repository.countByNickName(userDto.getLogin()) > 0) return false;
        if (repository.countByPhone(userDto.getPhone()) > 0) return false;
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User byUserName = repository.findByNickName(s);
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + byUserName.getRole().toString()));
        return new org.springframework.security.core.userdetails.User(
                byUserName.getNickName(),
                byUserName.getPassword(),
                simpleGrantedAuthorities
        );
    }

    public String getUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return username;
    }

    @Override
    public User getCurrentUser() {
        return repository.findByNickName(getUserName());
    }

    @Override
    public User getOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public User getByNickName(String nickName) {
        return repository.findByNickName(nickName);
    }

    @Override
    public void editUser(User user,MultipartFile file) {
        if(file != null && !file.isEmpty()) {
            String extention = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String fileName = UUID.randomUUID().toString()+extention;
            try {
                file.transferTo(new File(path+fileName));
                user.setPhotoUrl(fileName);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }
        save(user);
    }

    @Override
    public void delete(Long id) {
       repository.deleteById(id);
    }

    @Override
    public User giveAdmin(Long id) {
        User user = getOne(id);
        user.setRole(Role.ADMIN);
        return save(user);
    }

    @Override
    public User giveUser(Long id) {
        User user = getOne(id);
        user.setRole(Role.USER);
        return save(user);
    }

    @Override
    public Integer countLikes(Post post) {
        Integer likes = repository.countUsersByLikedPosts(post);
        return likes;
    }

    @Override
    public List<User> deleteFromFriends(User user) {
        List<User> friends = user.getFriended();
        User currentUser = getCurrentUser();
        friends.remove(currentUser);
        save(currentUser);
        currentUser.getFriends().add(user);
        return friends;
    }

    @Override
    public List<User> getAllFriendsByUserId(User user) {
        List<User> friends = repository.getAllByFriended(user);
        return friends;
    }

    @Override
    public List<User> addToFriends(User user) {
        List<User> friends = user.getFriended();
        User currentUser = getCurrentUser();
        if (friends.contains(currentUser)){
            friends.remove(currentUser);
            save(currentUser);
            currentUser.getFriends().add(user);
        }else {
            friends.add(currentUser);
            save(currentUser);
            currentUser.getFriends().add(user);
        }
        return friends;


    }

}
