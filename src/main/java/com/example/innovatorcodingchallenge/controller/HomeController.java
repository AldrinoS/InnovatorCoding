package com.example.innovatorcodingchallenge.controller;

import com.example.innovatorcodingchallenge.model.UserEntity;
import com.example.innovatorcodingchallenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<?> getUsers() {
        List<UserEntity> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<?> getServerStatus() {
        return new ResponseEntity<>("Service is UP", HttpStatus.OK);
    }
}
