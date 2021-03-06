package com.blog.service.impl;

import com.blog.dto.PostDto;
import com.blog.entity.Post;
import com.blog.entity.User;
import com.blog.repository.PostRepository;
import com.blog.service.PostService;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository repository;

    @Autowired
    private UserService service;

    @Override
    public Post save(Post post) {
        return repository.save(post);
    }

    @Override
    public Post getOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public List<Post> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Post> getAllByUserId(Long id) {
            return repository.getAllByUserId(id);
    }



    @Override
    public List<User> addUserToList(Post post) {
        List<User> users = post.getLiked();
        User user = service.getCurrentUser();
        if(users.contains(user)){
            users.remove(user);
            service.save(user);
            user.getLikedPosts().add(post);
        }else{
            users.add(user);
            service.save(user);
            user.getLikedPosts().add(post);
        }
        return users;
    }

    @Override
    public List<PostDto> postToDto(List<Post> posts) {
        List<PostDto> postDtos = new ArrayList<>();
        for(Post post: posts){
            postDtos.add(new PostDto(post));
        }
        return postDtos;
    }

    @Override
    public void addPost(String text, User user) {
        Post post = new Post(text, user);
        save(post);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Post edit(Post post) {
        return null;
    }


}
