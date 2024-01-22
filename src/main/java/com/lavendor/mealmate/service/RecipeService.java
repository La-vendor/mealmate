package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.model.RecipeIngredient;
import com.lavendor.mealmate.repository.BasicIngredientRepository;
import com.lavendor.mealmate.repository.RecipeIngredientRepository;
import com.lavendor.mealmate.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
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

    public Recipe createRecipe(String recipeName, List<RecipeIngredient> recipeIngredients, Long userId){
        Optional<Recipe> optionalRecipe = recipeRepository.findByRecipeNameAndUserId(recipeName,userId);

        if(optionalRecipe.isEmpty()) {
            Recipe recipe = new Recipe(recipeName, new ArrayList<>(), userId);

            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                Optional<BasicIngredient> optionalBasicIngredient = basicIngredientRepository.findById(recipeIngredient.getBasicIngredient().getBasicIngredientId());
                if(optionalBasicIngredient.isPresent()){
                    BasicIngredient existingIngredient = optionalBasicIngredient.get();
                    RecipeIngredient newRecipeIngredient = new RecipeIngredient(existingIngredient, recipeIngredient.getQuantity(), recipe);
                    recipe.getIngredients().add(newRecipeIngredient);
                }
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

    //TODO check if Recipe is not in use in DailyMenu
    public void deleteRecipe(Long recipeId){
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAllByRecipe_RecipeId(recipeId);
        recipeIngredientRepository.deleteAll(recipeIngredientList);
        recipeRepository.delete(recipe);
    }

    public List<Recipe> getRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserId(userId);
    }

    public static Recipe copyRecipe(Recipe originalRecipe, Long newUserId) {
        if (originalRecipe == null) {
            return null;
        }

        Recipe copiedRecipe = new Recipe();
        BeanUtils.copyProperties(originalRecipe, copiedRecipe);

        copiedRecipe.setUserId(newUserId);

        // Copy RecipeIngredients
        List<RecipeIngredient> copiedRecipeIngredients = new ArrayList<>();
        for (RecipeIngredient originalIngredient : originalRecipe.getIngredients()) {
            RecipeIngredient copiedIngredient = new RecipeIngredient();
            BeanUtils.copyProperties(originalIngredient, copiedIngredient);

            // Create a new BasicIngredient copy with the new userId
            BasicIngredient copiedBasicIngredient = new BasicIngredient();
            BeanUtils.copyProperties(originalIngredient.getBasicIngredient(), copiedBasicIngredient);
            copiedBasicIngredient.setUserId(newUserId);

            // Set the new recipe and basic ingredient for the copied recipe ingredient
            copiedIngredient.setRecipe(copiedRecipe);
            copiedIngredient.setBasicIngredient(copiedBasicIngredient);

            copiedRecipeIngredients.add(copiedIngredient);
        }

        copiedRecipe.setIngredients(copiedRecipeIngredients);

        return copiedRecipe;
    }

}