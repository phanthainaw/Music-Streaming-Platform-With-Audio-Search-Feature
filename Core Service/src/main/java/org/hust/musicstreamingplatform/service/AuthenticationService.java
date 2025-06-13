package org.hust.musicstreamingplatform.service;

import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.authentication.AuthenticationRequest;
import org.hust.musicstreamingplatform.dto.authentication.AuthenticationResponse;
import org.hust.musicstreamingplatform.dto.authentication.RegisterRequest;
import org.hust.musicstreamingplatform.dto.user.UserRegistrationDto;
import org.hust.musicstreamingplatform.model.User;
import org.hust.musicstreamingplatform.model.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;


    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder().token(token).build();
    }

    public void register(RegisterRequest registerRequest) {
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().username(registerRequest.getUsername()).password(registerRequest.getPassword()).name(registerRequest.getName()).email(registerRequest.getEmail()).role(Role.LISTENER).build();
        userService.registerUser(userRegistrationDto);
    }
}



