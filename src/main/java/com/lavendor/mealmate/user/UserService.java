package com.lavendor.mealmate.user;

import com.lavendor.mealmate.ingredient.BasicIngredientService;
import com.lavendor.mealmate.recipe.Recipe;
import com.lavendor.mealmate.security.PasswordEncoderService;
import com.lavendor.mealmate.ingredient.BasicIngredientRepository;
import com.lavendor.mealmate.recipe.RecipeService;
import com.lavendor.mealmate.recipeingredient.RecipeIngredientRepository;
import com.lavendor.mealmate.recipe.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
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
        if (userRepository.findByUsername(userDTO.username()).isEmpty()) {
            User user = new User(userDTO.username(), encodedPassword);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("Username is not available");
        }
    }

    public boolean checkIfPasswordIsEqual(UserDTO userDTO) {
        return (userDTO.password().equals(userDTO.confirmPassword()));
    }

    public void addStarterDataToUser(User user) {
        Long userId = user.getUserId();
        List<Recipe> starterRecipes = recipeService.getStarterRecipes();

        for (Recipe starterRecipe : starterRecipes) {
            Recipe copiedRecipe = recipeService.copyRecipe(starterRecipe, userId);
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
