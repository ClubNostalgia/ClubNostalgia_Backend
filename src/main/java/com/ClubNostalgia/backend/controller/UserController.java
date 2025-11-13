package com.ClubNostalgia.backend.controller;

import com.ClubNostalgia.backend.service.interfaces.UserService;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.ClubNostalgia.backend.dto.request.UserRequest;
import com.ClubNostalgia.backend.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<UserResponse> createAdmin(@Valid @RequestBody UserRequest userRequest) {
        UserResponse responseDTO = userService.createAdmin(userRequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<UserResponse> getUserByName(@PathVariable String name) {
        return new ResponseEntity<>(userService.getUserByName(name), HttpStatus.OK);
    }
}