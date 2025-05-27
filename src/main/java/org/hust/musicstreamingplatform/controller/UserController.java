package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.user.UpdateUserInfoRequest;
import org.hust.musicstreamingplatform.dto.user.UserDto;
import org.hust.musicstreamingplatform.dto.user.UserRegistrationDto;
import org.hust.musicstreamingplatform.model.User;
import org.hust.musicstreamingplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.hust.musicstreamingplatform.utils.Utils.getUserFromPrincipal;

@RestController
@RequestMapping("/users")
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

//    @PreAuthorize("hasRole('LISTENER')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserInfo(Principal principal, @PathVariable int id, @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        if (getUserFromPrincipal(principal).getId()!=id) return ResponseEntity.status(403).build();
        userService.updateUserInfo(id, updateUserInfoRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@RequestBody UserRegistrationDto userRegistrationDto ) {


        return ResponseEntity.ok("");
    }



}
