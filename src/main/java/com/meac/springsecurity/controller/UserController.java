package com.meac.springsecurity.controller;

import com.meac.springsecurity.controller.dto.CreateUserDTO;
import com.meac.springsecurity.entities.Role;
import com.meac.springsecurity.entities.User;
import com.meac.springsecurity.repositories.RoleRepository;
import com.meac.springsecurity.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.meac.springsecurity.repositories.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@Tag(name = "Usuários", description = "Operações relacionadas a gerenciamento de usuários")
public class UserController {

    private final UserServices userService;

    public UserController(UserServices userService) {
        this.userService = userService;

    }


    @Transactional
    @PostMapping("/users")
    @Operation(summary = "Cadastrar", description = "Cadastrar um novo usuário")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDTO userDTO) {
        try {
            userService.createUser(userDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Todos os usuários", description = "Listar todos os usuários (Apenas usuários do tipo ADMIN)")
    public ResponseEntity<List<User>> listUsers() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}