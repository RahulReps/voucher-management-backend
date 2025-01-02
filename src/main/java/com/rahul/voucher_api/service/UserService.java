package com.rahul.voucher_api.service;

import com.rahul.voucher_api.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User registerUser(User user);
    Optional<User> authenticateUser(String email, String password);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    void deleteById(UUID id);
    List<User> getAllUsers();
}