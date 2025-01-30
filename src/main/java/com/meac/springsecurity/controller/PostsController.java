package com.meac.springsecurity.controller;

import com.meac.springsecurity.controller.dto.CreatePostDTO;
import com.meac.springsecurity.controller.dto.FeedDTO;
import com.meac.springsecurity.controller.dto.FeedItemDTO;
import com.meac.springsecurity.entities.Post;
import com.meac.springsecurity.entities.Role;
import com.meac.springsecurity.repositories.PostRepository;
import com.meac.springsecurity.repositories.UserRepository;
import com.meac.springsecurity.services.PostsServices;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PostsController {

    public final PostsServices postsServices;

    public PostsController(PostsServices postsServices) {
        this.postsServices = postsServices;
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDTO> getAllPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        Page<FeedItemDTO> paginatedPosts = postsServices.getPaginatedPosts(page, pageSize);
        return ResponseEntity.ok(FeedDTO.fromPage(paginatedPosts));
    }

    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody CreatePostDTO postDTO, JwtAuthenticationToken  jwtToken) {
        try {
            postsServices.createNewPost(postDTO, jwtToken);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping(value = "/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id, JwtAuthenticationToken jwtToken) {

        try {
            postsServices.deletePost(id, jwtToken);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }
}
