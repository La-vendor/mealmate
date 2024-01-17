package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.User;
import com.lavendor.mealmate.model.UserDTO;
import com.lavendor.mealmate.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    public UserService(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    public User createUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.username() == null || userDTO.password() == null) {
            throw new RuntimeException("Invalid user object");
        }

        if (!userDTO.password().equals(userDTO.confirmPassword())) {
            throw new RuntimeException("Password and confirm password do not match");
        }
        String encodedPassword = passwordEncoderService.encodePassword(userDTO.password());
        User user = new User(userDTO.username(), encodedPassword);

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Username is not available", e);
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.findByUsername(username).isPresent();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public Long getActiveUserId(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<User> optionalUser = userRepository.findByUsername(authentication.getName());
            if (optionalUser.isPresent()) return optionalUser.get().getUserId();
        }
        return null;
    }

}
