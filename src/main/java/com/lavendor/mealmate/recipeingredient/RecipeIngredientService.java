package com.lavendor.mealmate.recipeingredient;

import com.lavendor.mealmate.ingredient.BasicIngredient;
import com.lavendor.mealmate.recipe.Recipe;
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

    public void addRecipeToIngredients(List<RecipeIngredient> recipeIngredients, Recipe recipe) {
        for(RecipeIngredient recipeIngredient : recipeIngredients){
            recipeIngredient.setRecipe(recipe);
        }
        recipeIngredientRepository.saveAll(recipeIngredients);
    }
}
