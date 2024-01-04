package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.model.RecipeIngredient;
import com.lavendor.mealmate.repository.RecipeIngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

private final RecipeIngredientRepository recipeIngredientRepository;

    public IngredientService(RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public RecipeIngredient addIngredient(BasicIngredient basicIngredient, double quantity, String unit){
        RecipeIngredient recipeIngredient = new RecipeIngredient(basicIngredient,quantity,unit);
        return recipeIngredientRepository.save(recipeIngredient);
    }

    public RecipeIngredient getIngredientById(Long ingredientId){
        return recipeIngredientRepository.findById(ingredientId).orElseThrow(()-> new EntityNotFoundException("Ingredient not found"));
    }

    public RecipeIngredient getIngredientByName(String recipeIngredientName){
        BasicIngredient basicIngredient = new BasicIngredient(recipeIngredientName);
        return recipeIngredientRepository.findByBasicIngredient(basicIngredient).orElseThrow(()-> new EntityNotFoundException("Ingredient not found"));
    }

    public List<RecipeIngredient> getAllIngredients(){
        return recipeIngredientRepository.findAll();
    }

    public void deleteIngredient(Long ingredientId){
        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(ingredientId).orElseThrow(()-> new EntityNotFoundException("Ingredient not found"));
        recipeIngredientRepository.delete(recipeIngredient);
    }

}
