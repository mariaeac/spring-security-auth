package com.meac.springsecurity.controller;

import com.meac.springsecurity.controller.dto.CreatePostDTO;
import com.meac.springsecurity.entities.Post;
import com.meac.springsecurity.repositories.PostRepository;
import com.meac.springsecurity.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PostsController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostsController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody CreatePostDTO postDTO, JwtAuthenticationToken  jwtToken) {
        // Recebe o usuário logado:
        var user = userRepository.findById(UUID.fromString(jwtToken.getName()));

        var newPost = new Post();
        newPost.setUser(user.get());
        newPost.setContent(postDTO.content());

        postRepository.save(newPost);
        return ResponseEntity.ok().build();

    }
}
