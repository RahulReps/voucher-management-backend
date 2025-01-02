package com.rahul.voucher_api.controller;

import com.rahul.voucher_api.entity.User;
import com.rahul.voucher_api.service.UserService;
import com.rahul.voucher_api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        user.setOrderCount(0);
        User registeredUser = userService.registerUser(user);
        String token = jwtUtil.generateToken(registeredUser);

        Map<String, Object> response = new HashMap<>();
        response.put("user", registeredUser);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> authenticatedUser = userService.authenticateUser(email, password);

        if (authenticatedUser.isPresent()) {
            String token = jwtUtil.generateToken(authenticatedUser.get());

            Map<String, Object> response = new HashMap<>();
            response.put("user", authenticatedUser.get());
            response.put("token", token);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isValidToken(authHeader)) {
            return ResponseEntity.status(401).build();
        }

        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id,
                                               @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isValidToken(authHeader)) {
            return ResponseEntity.status(401).build();
        }

        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    private boolean isValidToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);
        return jwtUtil.validateTokenAndGetEmail(token) != null;
    }
}