package com.example.innovatorcodingchallenge.controller;

import com.example.innovatorcodingchallenge.dto.LoginDto;
import com.example.innovatorcodingchallenge.dto.RegisterDto;
import com.example.innovatorcodingchallenge.model.Roles;
import com.example.innovatorcodingchallenge.model.UserEntity;
import com.example.innovatorcodingchallenge.repository.RoleRepository;
import com.example.innovatorcodingchallenge.repository.UserRepository;
import com.example.innovatorcodingchallenge.security.JwtGenerator;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    JwtGenerator jwtGenerator;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );
        }catch (Exception e) {
            return new ResponseEntity<>("Login failed", HttpStatus.BAD_REQUEST);
        }

        String token = jwtGenerator.generate(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto, @RequestParam String authLevel) {
        if(userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Optional<List<Roles>> roles = roleRepository.findByName(authLevel);
        if(roles.isPresent()) {
            userEntity.setRoles(roles.get());
        } else {
            return new ResponseEntity<>("Selected Auth Level not Available", HttpStatus.BAD_REQUEST);
        }

        userRepository.save(userEntity);

        return new ResponseEntity<>("User is created", HttpStatus.CREATED);
    }

}
