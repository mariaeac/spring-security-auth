package com.meac.springsecurity.controller;

import com.meac.springsecurity.controller.dto.CreatePostDTO;
import com.meac.springsecurity.controller.dto.FeedDTO;
import com.meac.springsecurity.controller.dto.FeedItemDTO;
import com.meac.springsecurity.entities.Post;
import com.meac.springsecurity.entities.Role;
import com.meac.springsecurity.repositories.PostRepository;
import com.meac.springsecurity.repositories.UserRepository;
import com.meac.springsecurity.services.PostsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Posts", description = "Operações relacionadas a posts")
public class PostsController {

    public final PostsServices postsServices;

    public PostsController(PostsServices postsServices) {
        this.postsServices = postsServices;
    }

    @GetMapping("/feed")
    @Operation(summary = "Feed geral", description = "Retorna o feed com todos os posts")
    public ResponseEntity<FeedDTO> getAllPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        Page<FeedItemDTO> paginatedPosts = postsServices.getPaginatedPosts(page, pageSize);
        return ResponseEntity.ok(FeedDTO.fromPage(paginatedPosts));
    }

    @PostMapping("/posts")
    @Operation(summary = "Criar post", description = "Crie um novo post")

    public ResponseEntity<Void> createPost(@RequestBody CreatePostDTO postDTO, JwtAuthenticationToken  jwtToken) {
        try {
            postsServices.createNewPost(postDTO, jwtToken);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping(value = "/posts/{id}")
    @Operation(summary = "Deletar post", description = "Deletar post pelo ID")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id, JwtAuthenticationToken jwtToken) {

        try {
            postsServices.deletePost(id, jwtToken);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }
}
