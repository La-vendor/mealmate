package com.lavendor.mealmate.recipe;

import com.lavendor.mealmate.ingredient.BasicIngredient;
import com.lavendor.mealmate.recipeingredient.RecipeIngredient;
import com.lavendor.mealmate.ingredient.BasicIngredientRepository;
import com.lavendor.mealmate.recipeingredient.RecipeIngredientRepository;
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
    private final BasicIngredientRepository basicIngredientRepository;

    public RecipeService(RecipeRepository recipeRepository, RecipeIngredientRepository recipeIngredientRepository,
                         BasicIngredientRepository basicIngredientRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.basicIngredientRepository = basicIngredientRepository;
    }

    public Recipe createRecipe(String recipeName, List<RecipeIngredient> recipeIngredients, Long userId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findByRecipeNameAndUserId(recipeName, userId);

        if (optionalRecipe.isEmpty()) {
            Recipe recipe = new Recipe(recipeName, new ArrayList<>(), userId);

            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                Optional<BasicIngredient> optionalBasicIngredient = basicIngredientRepository.findById(recipeIngredient.getBasicIngredient().getBasicIngredientId());
                if (optionalBasicIngredient.isPresent()) {
                    BasicIngredient existingIngredient = optionalBasicIngredient.get();
                    RecipeIngredient newRecipeIngredient = new RecipeIngredient(existingIngredient, recipeIngredient.getQuantity(), recipe);
                    recipe.getIngredients().add(newRecipeIngredient);
                }
            }
            return recipeRepository.save(recipe);
        } else {
            throw new DataIntegrityViolationException("Recipe with this name already exists");
        }
    }

    public List<Recipe> getStarterRecipes() {
        List<Recipe> recipesToAdd = new ArrayList<>();

        for (long i = 1L; i <= 7; i++) {
            Recipe existingRecipe = recipeRepository.findById(i).orElse(null);
            if (existingRecipe != null) {
                recipesToAdd.add(existingRecipe);
            }
        }
        return recipesToAdd;
    }

    public Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    //TODO check if Recipe is not in use in DailyMenu
    public void deleteRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAllByRecipe_RecipeId(recipeId);
        recipeIngredientRepository.deleteAll(recipeIngredientList);
        recipeRepository.delete(recipe);
    }

    public List<Recipe> getRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserId(userId);
    }

    public Recipe copyRecipe(Recipe originalRecipe, Long newUserId) {
        if (originalRecipe == null) {
            return null;
        }
        Recipe copiedRecipe = new Recipe();
        copiedRecipe.setRecipeName(originalRecipe.getRecipeName());
        copiedRecipe.setUserId(newUserId);
        copiedRecipe = recipeRepository.save(copiedRecipe);

        // Copy RecipeIngredients
        List<RecipeIngredient> copiedRecipeIngredients = new ArrayList<>();
        for (RecipeIngredient originalIngredient : originalRecipe.getIngredients()) {
            RecipeIngredient copiedIngredient = new RecipeIngredient(originalIngredient);
            copiedIngredient.setRecipe(copiedRecipe);

            Optional<BasicIngredient> optionalBasicIngredient = basicIngredientRepository.findByBasicIngredientNameAndUserId(
                    originalIngredient.getBasicIngredient().getBasicIngredientName(), newUserId);
            BasicIngredient basicIngredient;

            if (optionalBasicIngredient.isPresent()) {
                basicIngredient = optionalBasicIngredient.get();
            } else {
                basicIngredient = new BasicIngredient(originalIngredient.getBasicIngredient());
                basicIngredient.setUserId(newUserId);
            }

            // Set the new recipe and basic ingredient for the copied recipe ingredient
            copiedIngredient.setBasicIngredient(basicIngredient);

            copiedRecipeIngredients.add(copiedIngredient);
        }

        copiedRecipe.setIngredients(copiedRecipeIngredients);

        return copiedRecipe;
    }

}