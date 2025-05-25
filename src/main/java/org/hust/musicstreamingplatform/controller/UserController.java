package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.user.UserDto;
import org.hust.musicstreamingplatform.dto.user.UserRegistrationDto;
import org.hust.musicstreamingplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRegistrationDto userRegistrationDto ) {
        userService.registerUser(userRegistrationDto);
        return ResponseEntity.ok("User registered successfully");

    }


}
