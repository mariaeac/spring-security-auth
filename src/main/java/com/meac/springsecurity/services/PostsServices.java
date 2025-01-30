package com.meac.springsecurity.services;

import com.meac.springsecurity.controller.dto.CreatePostDTO;
import com.meac.springsecurity.controller.dto.FeedDTO;
import com.meac.springsecurity.controller.dto.FeedItemDTO;
import com.meac.springsecurity.entities.Post;
import com.meac.springsecurity.entities.Role;
import com.meac.springsecurity.repositories.PostRepository;
import com.meac.springsecurity.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PostsServices {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostsServices(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void createNewPost(CreatePostDTO postDTO, JwtAuthenticationToken jwtToken) {
        var loggedUser = userRepository.findById(UUID.fromString(jwtToken.getName()));  // Pega o usuário logado
        Post newPost = new Post();
        newPost.setUser(loggedUser.get());
        newPost.setContent(postDTO.content());
        postRepository.save(newPost);
    }

    public void deletePost(Long id, JwtAuthenticationToken jwtToken) {
        var loggedUser = userRepository.findById(UUID.fromString(jwtToken.getName())); // Pega o usuário logado
        boolean isAdmin = loggedUser.get().getRoles().stream().anyMatch(role -> role.getName().equals(Role.Values.ADMIN.name())); // Verifica se o usuário é administrador ou basic

        if (isAdmin) {
            postRepository.deleteById(id);
        } else {
            var post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            if (!post.getUser().getUserId().equals(UUID.fromString(jwtToken.getName()))) { // Verifica se o post pertence ao usuário
               throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            postRepository.deleteById(id);


        }

    }

    public Page<FeedItemDTO> getPaginatedPosts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimeStamp");
        return postRepository.findAll(pageable)
                .map(post -> new FeedItemDTO(
                        post.getPostId(),
                        post.getContent(),
                        post.getUser().getUsername()
                ));
    }

}
