package org.hust.musicstreamingplatform.controller;

import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.authentication.AuthenticationRequest;
import org.hust.musicstreamingplatform.dto.authentication.AuthenticationResponse;
import org.hust.musicstreamingplatform.dto.authentication.RegisterRequest;
import org.hust.musicstreamingplatform.exception.authentication.UsernameExisted;
import org.hust.musicstreamingplatform.model.User;
import org.hust.musicstreamingplatform.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor()

public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestParam("username") String username,
                                                        @RequestParam("password") String password) {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().username(username).password(password).build();
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody RegisterRequest registerRequest) {
        try {
            authenticationService.register(registerRequest);
        } catch (UsernameExisted e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        return ResponseEntity.ok("User registered successfully");
    }

}
