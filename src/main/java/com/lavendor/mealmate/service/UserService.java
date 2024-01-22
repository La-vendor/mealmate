package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.model.User;
import com.lavendor.mealmate.model.UserDTO;
import com.lavendor.mealmate.repository.BasicIngredientRepository;
import com.lavendor.mealmate.repository.RecipeIngredientRepository;
import com.lavendor.mealmate.repository.RecipeRepository;
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
    private final RecipeService recipeService;
    private final BasicIngredientService basicIngredientService;
    private final RecipeRepository recipeRepository;
    private final BasicIngredientRepository basicIngredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public UserService(UserRepository userRepository, PasswordEncoderService passwordEncoderService, RecipeService recipeService, BasicIngredientService basicIngredientService,
                       RecipeRepository recipeRepository,
                       BasicIngredientRepository basicIngredientRepository,
                       RecipeIngredientRepository recipeIngredientRepository) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.recipeService = recipeService;
        this.basicIngredientService = basicIngredientService;
        this.recipeRepository = recipeRepository;
        this.basicIngredientRepository = basicIngredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public User createUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.username() == null || userDTO.password() == null) {
            throw new RuntimeException("Invalid user object");
        }

        String encodedPassword = passwordEncoderService.encodePassword(userDTO.password());
        User user = new User(userDTO.username(), encodedPassword);

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            //TODO Add custom exception - UserAlreadyExistsException
            throw new RuntimeException("Username is not available", e);
        }
    }

    public boolean checkIfPasswordIsEqual(UserDTO userDTO) {
        return (userDTO.password().equals(userDTO.confirmPassword()));
    }

    public void addStarterDataToUser(User user) {
        Long userId = user.getUserId();
        List<Recipe> starterRecipes = recipeService.getStarterRecipes();

        for (Recipe starterRecipe : starterRecipes) {
            Recipe copiedRecipe = RecipeService.copyRecipe(starterRecipe, userId);
            recipeRepository.save(copiedRecipe);
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
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
