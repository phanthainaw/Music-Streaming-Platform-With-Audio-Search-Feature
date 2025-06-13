package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.user.UpdateUserInfoRequest;
import org.hust.musicstreamingplatform.dto.user.UpdateUserRoleRequest;
import org.hust.musicstreamingplatform.dto.user.UserDto;
import org.hust.musicstreamingplatform.dto.user.UserRegistrationDto;
import org.hust.musicstreamingplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.hust.musicstreamingplatform.utils.Utils.getUserFromPrincipal;

@RestController
@RequestMapping("/users")

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        int currentUserId = getUserFromPrincipal(principal).getId();
        return ResponseEntity.ok(userService.getUserById(currentUserId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRegistrationDto userRegistrationDto ) {
        userService.registerUser(userRegistrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PreAuthorize("hasAnyAuthority('LISTENER','ADMIN', 'PUBLISHER')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserInfo(Principal principal, @PathVariable int id, @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        if (getUserFromPrincipal(principal).getId()!=id) return ResponseEntity.status(403).build();
        userService.updateUserInfo(id, updateUserInfoRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@RequestBody UserRegistrationDto userRegistrationDto ) {


        return ResponseEntity.ok("");
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> updateUserRole(@PathVariable int id,@RequestBody UpdateUserRoleRequest updateUserRoleRequest) {
        userService.UpdateUserRole(id, updateUserRoleRequest);
        return ResponseEntity.ok().build();
    }



}
