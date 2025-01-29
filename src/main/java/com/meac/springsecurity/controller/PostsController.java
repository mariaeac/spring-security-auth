package com.meac.springsecurity.controller;

import com.meac.springsecurity.controller.dto.CreatePostDTO;
import com.meac.springsecurity.controller.dto.FeedDTO;
import com.meac.springsecurity.controller.dto.FeedItemDTO;
import com.meac.springsecurity.entities.Post;
import com.meac.springsecurity.entities.Role;
import com.meac.springsecurity.repositories.PostRepository;
import com.meac.springsecurity.repositories.UserRepository;
import org.apache.coyote.Response;
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
public class PostsController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostsController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/feed")
    public ResponseEntity<List<FeedDTO>> getAllPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
                                                     ) {
       var posts =  postRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimeStamp")).map(post -> new FeedItemDTO(post.getPostId(), post.getContent(), post.getUser().getUsername()));
       return ResponseEntity.ok(Collections.singletonList(new FeedDTO(posts.getContent(), page, pageSize, posts.getTotalPages(), posts.getTotalElements())));

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

        // Verificação para ver se o usuário é admin
        var user = userRepository.findById(UUID.fromString(jwtToken.getName()));
        boolean isAdmin = user.get().getRoles().stream().anyMatch(role -> role.getName().equals(Role.Values.ADMIN.name()));

        if(isAdmin) {
            postRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            var post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            // Verifica se o post pertence ao usuário logado:
            if (!post.getUser().getUserId().equals(UUID.fromString(jwtToken.getName()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            postRepository.deleteById(id);

            return ResponseEntity.ok().build();
        }




    }
}
