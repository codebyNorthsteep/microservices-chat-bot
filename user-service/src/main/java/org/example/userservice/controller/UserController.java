package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.CreateUserRequest;
import org.example.userservice.dto.UpdateUserRequest;
import org.example.userservice.dto.UserDto;
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
    ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest user) {
        UserDto created = userService.createUser(user);
        log.info("User created with id: {}", created.id());
        return ResponseEntity.created(URI.create("/api/users/" + created.id())).body(created);
    }

    @GetMapping("/users/{id}")
    ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        log.info("Fetching user with id: {}", id);
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/users/{id}")
    ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Name") String authenticatedUser, // <-- Fånga headern från BFF
            @Valid @RequestBody UpdateUserRequest user) {

        UserDto updated = userService.updateUser(id, user, authenticatedUser);
        log.info("Updating user with id: {}", id);
        return ResponseEntity.ok(updated);
    }

    //todo: skapa en PATCH endpoint för att uppdatera lösenord vid tillfälle

    @DeleteMapping("/users/{id}")
    ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Name") String authenticatedUser) { // <-- Fånga headern från BFF

        log.info("Deleting user with id: {}", id);
        // Säkerhetscheck: Skicka med det verifierade namnet in i servicen
        userService.deleteUser(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

}
