package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.model.RecipeIngredient;
import com.lavendor.mealmate.repository.RecipeIngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public RecipeIngredient addRecipeIngredient(BasicIngredient basicIngredient, double quantity, Recipe recipe) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(basicIngredient, quantity, recipe);
        return recipeIngredientRepository.save(recipeIngredient);
    }

    public RecipeIngredient getRecipeIngredientById(Long ingredientId) {
        return recipeIngredientRepository.findById(ingredientId).orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
    }

    public List<RecipeIngredient> getAllRecipeIngredients() {
        return recipeIngredientRepository.findAll();
    }

    public void deleteRecipeIngredient(Long ingredientId) {
        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(ingredientId).orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
        recipeIngredientRepository.delete(recipeIngredient);
    }

}
