package com.meac.springsecurity.controller;

import com.meac.springsecurity.controller.dto.LoginResponse;
import com.meac.springsecurity.entities.Role;
import com.meac.springsecurity.entities.User;
import com.meac.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.meac.springsecurity.controller.dto.LoginRequest;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public TokenController(UserRepository userRepository, JwtEncoder jwtEncoder, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        var instantNow = Instant.now();
        var expireTime = 300L;



        // Verifica se o login é válido
        var user = userRepository.findByUsername(loginRequest.username());

        var roles = user.get().getRoles()
                .stream()
                .map(role -> role.getName().toUpperCase()) // ADMIN
                .collect(Collectors.toList());


        if (user.isEmpty() || !user.get().isLoginValid(loginRequest, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Gera o token JWT
        var claims = JwtClaimsSet.builder().issuer("meac-backend")
                .subject(user.get().getUserId().toString())
                .issuedAt(instantNow)
                .expiresAt(instantNow.plusSeconds(expireTime))
                .claim("roles", roles)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expireTime));






    }
}


