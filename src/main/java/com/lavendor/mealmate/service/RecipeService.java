package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.model.RecipeIngredient;
import com.lavendor.mealmate.repository.RecipeIngredientRepository;
import com.lavendor.mealmate.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeService(RecipeRepository recipeRepository, RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public Recipe createRecipe(String recipeName, List<RecipeIngredient> recipeIngredients, Long userId){
        Optional<Recipe> optionalRecipe = recipeRepository.findByRecipeName(recipeName);

        if(optionalRecipe.isEmpty()) {
            Recipe recipe = new Recipe(recipeName, recipeIngredients, userId);
            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                recipeIngredient.setRecipe(recipe);
            }
            return recipeRepository.save(recipe);
        }else{
            throw new DataIntegrityViolationException("Recipe with this name already exists");
        }
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

    public List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

    public void deleteRecipe(Long recipeId){
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAllByRecipe_RecipeId(recipeId);
        recipeIngredientRepository.deleteAll(recipeIngredientList);
        recipeRepository.delete(recipe);
    }

    public List<Recipe> getRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserId(userId);
    }

}