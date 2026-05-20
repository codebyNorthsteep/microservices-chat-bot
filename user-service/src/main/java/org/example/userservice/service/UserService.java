package org.example.userservice.service;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.example.userservice.dto.CreateUserRequest;
import org.example.userservice.dto.UpdateUserRequest;
import org.example.userservice.dto.UserResponse;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public UserResponse createUser(CreateUserRequest request) {
        // Hash the password before saving
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User saved = userRepository.save(user);
            return userMapper.toUserResponse(saved);

        } catch (DataIntegrityViolationException _) {
            throw new RuntimeException("Username already taken");
        }
    }

    public UserResponse getUser(Long id) {
        User user = findUserById(id);

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest updatedUser) {
        User existingUser = findUserById(id);
        userMapper.updateEntity(updatedUser, existingUser);
        User saved = userRepository.save(existingUser);
        return userMapper.toUserResponse(saved);
    }

    public User updateUserPassword(Long id, String updatedPassword) {
        User existingUser = findUserById(id);

        if (updatedPassword == null || updatedPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        existingUser.setPassword(passwordEncoder.encode(updatedPassword));
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}
