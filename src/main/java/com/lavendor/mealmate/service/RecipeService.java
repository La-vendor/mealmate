package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.RecipeIngredient;
import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe createRecipe(String recipeName, List<RecipeIngredient> recipeIngredients){
        Recipe recipe = new Recipe(recipeName, recipeIngredients);
        return recipeRepository.save(recipe);
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
}
