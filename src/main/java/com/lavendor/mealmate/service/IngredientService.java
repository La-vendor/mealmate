package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.Ingredient;
import com.lavendor.mealmate.repository.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient addIngredient(String ingredientName, double quantity, String unit){
        Ingredient ingredient = new Ingredient(ingredientName,quantity,unit);
        return ingredientRepository.save(ingredient);
    }

    public Ingredient getIngredientById(Long ingredientId){
        return ingredientRepository.findById(ingredientId).orElseThrow(()-> new EntityNotFoundException("Ingredient not found"));
    }

    public Ingredient getIngredientByName(String ingredientName){
        return ingredientRepository.findByIngredientName(ingredientName).orElseThrow(()-> new EntityNotFoundException("Ingredient not found"));
    }

    public List<Ingredient> getAllIngredients(){
        return ingredientRepository.findAll();
    }

    public void deleteIngredient(Long ingredientId){
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(()-> new EntityNotFoundException("Ingredient not found"));
        ingredientRepository.delete(ingredient);
    }

}
