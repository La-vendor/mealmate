package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.model.RecipeIngredient;
import com.lavendor.mealmate.repository.BasicIngredientRepository;
import com.lavendor.mealmate.repository.RecipeRepository;
import com.lavendor.mealmate.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final BasicIngredientRepository basicIngredientRepository;

    public RecipeService(RecipeRepository recipeRepository,
                         UserRepository userRepository, BasicIngredientRepository basicIngredientRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.basicIngredientRepository = basicIngredientRepository;
    }

    public Recipe createRecipe(String recipeName, List<RecipeIngredient> recipeIngredients, Long userId){
        Recipe recipe = new Recipe(recipeName, recipeIngredients, userId);
        for(RecipeIngredient recipeIngredient : recipeIngredients){
            recipeIngredient.setRecipe(recipe);
        }
        return recipeRepository.save(recipe);
    }

    public List<Recipe> getStarterRecipes(){
        List<Recipe> recipesToAdd = new ArrayList<>();

        for(long i=1L; i<=7; i++){
            Recipe existingRecipe = recipeRepository.findById(i).orElse(null);
            if (existingRecipe != null) {
                recipesToAdd.add(existingRecipe);
            }
        }
        return recipesToAdd;
    }

    public Recipe getRecipeById(Long recipeId){
        return recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
    }

    public Recipe getRecipeByName(String recipeName){
        return recipeRepository.findByRecipeName(recipeName).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
    }

    public List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

    public void deleteRecipe(Long recipeId){
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
        recipeRepository.delete(recipe);
    }


    public List<Recipe> getRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserId(userId);
    }
}
