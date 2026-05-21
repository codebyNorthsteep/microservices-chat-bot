package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.CreateUserRequest;
import org.example.userservice.dto.UpdateUserRequest;
import org.example.userservice.dto.UserResponse;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@Slf4j
@RestController
@RequestMapping("/api")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users")
    ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest user) {
        UserResponse created = userService.createUser(user);
        log.info("User created with id: {}", created.id());
        return ResponseEntity.created(URI.create("/api/users/" + created.id())).body(created);
    }

    @GetMapping("/users/{id}")
    ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        log.info("Fetching user with id: {}", id);
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/users/{id}")
    ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest user) {
        UserResponse updated = userService.updateUser(id, user);
        log.info("Updating user with id: {}", id);
        return ResponseEntity.ok(updated);
    }

    //todo: skapa en PATCH endpoint för att uppdatera lösenord vid tillfälle

    @DeleteMapping("/users/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
