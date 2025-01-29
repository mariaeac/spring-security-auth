package com.meac.springsecurity.controller;

import com.meac.springsecurity.controller.dto.CreatePostDTO;
import com.meac.springsecurity.entities.Post;
import com.meac.springsecurity.repositories.PostRepository;
import com.meac.springsecurity.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @DeleteMapping(value = "/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id, JwtAuthenticationToken jwtToken) {
        var post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // Verifica se o post pertence ao usuário logado:
        if (!post.getUser().getUserId().equals(UUID.fromString(jwtToken.getName()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        postRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
