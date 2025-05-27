package org.hust.musicstreamingplatform.service;

import jakarta.transaction.Transactional;
import org.hust.musicstreamingplatform.dto.user.UpdateUserInfoRequest;
import org.hust.musicstreamingplatform.dto.user.UpdateUserRoleRequest;
import org.hust.musicstreamingplatform.dto.user.UserDto;
import org.hust.musicstreamingplatform.dto.user.UserRegistrationDto;
import org.hust.musicstreamingplatform.exception.UserNotFoundException;
import org.hust.musicstreamingplatform.exception.authentication.UsernameExisted;
import org.hust.musicstreamingplatform.model.User;
import org.hust.musicstreamingplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .toList();
    }

    public UserDto getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return UserDto.builder()
                .name(user.getName())
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public void registerUser(UserRegistrationDto userRegistrationDto) {
        Optional<User> user = userRepository.findByUsername(userRegistrationDto.getUsername());
        if (user.isPresent()) {
            throw new UsernameExisted("Username already existed");
        }
        User newUser = new User();
        newUser.setName(userRegistrationDto.getName());
        newUser.setEmail(userRegistrationDto.getEmail());
        newUser.setUsername(userRegistrationDto.getUsername());
        newUser.setRole(userRegistrationDto.getRole());
        newUser.setPasswordHash(passwordEncoder.encode(userRegistrationDto.getPassword()));
        userRepository.save(newUser);
    }

    @Transactional
    public void updateUserInfo(int id, UpdateUserInfoRequest updateUserInfoRequest) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setName(updateUserInfoRequest.getName());
        user.setEmail(updateUserInfoRequest.getEmail());
        userRepository.save(user);
    }

    public void UpdateUserRole(int id,UpdateUserRoleRequest updateUserRoleRequest) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setRole(updateUserRoleRequest.getRole());
        userRepository.save(user);
    }

    public void deleteUser(int id) {


    }
}
