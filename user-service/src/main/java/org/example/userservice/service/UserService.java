package org.example.userservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.example.userservice.dto.CreateUserRequest;
import org.example.userservice.dto.UpdateUserRequest;
import org.example.userservice.dto.UserDto;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserDto createUser(CreateUserRequest request) {
        // Hash the password before saving
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User saved = userRepository.save(user);
            return userMapper.toUserResponse(saved);

        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Username or email already taken", ex);
        }
    }

    public UserDto getUser(Long id) {
        User user = findUserById(id);

        return userMapper.toUserResponse(user);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toUserResponse(user);
    }

    public UserDto updateUser(Long id, UpdateUserRequest updatedUser, String authenticatedUser) {
        User existingUser = findUserById(id);
        if (authenticatedUser != null && !existingUser.getUsername().equals(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own profile!");
        }
        userMapper.updateEntity(updatedUser, existingUser);
        try {
            User saved = userRepository.save(existingUser);
            return userMapper.toUserResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Username or email already taken", ex);
        }
    }

    //todo: skapa en PATCH endpoint för att uppdatera lösenord vid tillfälle
    public User updateUserPassword(Long id, String updatedPassword) {
        User existingUser = findUserById(id);

        if (updatedPassword == null || updatedPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        existingUser.setPassword(passwordEncoder.encode(updatedPassword));
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id, String authenticatedUser) {
        User existingUser = findUserById(id);
        if (authenticatedUser != null && !existingUser.getUsername().equals(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own profile!");
        }
        userRepository.deleteById(id);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}
