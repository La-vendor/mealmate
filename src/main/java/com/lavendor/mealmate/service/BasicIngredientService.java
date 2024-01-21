package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.repository.BasicIngredientRepository;
import com.lavendor.mealmate.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BasicIngredientService {

    private final BasicIngredientRepository basicIngredientRepository;
    private final RecipeRepository recipeRepository;

    public BasicIngredientService(BasicIngredientRepository basicIngredientRepository, RecipeRepository recipeRepository) {
        this.basicIngredientRepository = basicIngredientRepository;
        this.recipeRepository = recipeRepository;
    }

    public BasicIngredient addBasicIngredient(String basicIngredientName, String unit, Long userId){
        BasicIngredient basicIngredient = new BasicIngredient(basicIngredientName, unit, userId);
        return basicIngredientRepository.save(basicIngredient);
    }

    public BasicIngredient getBasicIngredientById(Long basicIngredientId){
        return basicIngredientRepository.findById(basicIngredientId).orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
    }

    public boolean checkIfIngredientExists(String basicIngredientName){
        return basicIngredientRepository.existsByBasicIngredientName(basicIngredientName);
    }

    public Optional<BasicIngredient> getBasicIngredientByNameAndUserId(String basicIngredientName, Long userId){
        return basicIngredientRepository.findByBasicIngredientNameAndUserId(basicIngredientName,userId);
    }

    public List<BasicIngredient> getAllBasicIngredients(){
        return basicIngredientRepository.findAll();
    }

    public void deleteBasicIngredient(Long basicIngredientId){
        BasicIngredient basicIngredient = basicIngredientRepository.findById(basicIngredientId).orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
        basicIngredientRepository.delete(basicIngredient);
    }

    public List<BasicIngredient> getBasicIngredientsByUserId(Long userId) {
        return basicIngredientRepository.findByUserId(userId);
    }

    public List<BasicIngredient> getStarterIngredients() {

        List<BasicIngredient> basicIngredientsToAdd = new ArrayList<>();
        Optional<BasicIngredient> optionalBasicIngredient;
        for(long i = 1L; i <=30; i++){
            optionalBasicIngredient = basicIngredientRepository.findById(i);
            optionalBasicIngredient.ifPresent(basicIngredientsToAdd::add);
        }
        return basicIngredientsToAdd;
    }

    public List<Recipe> getRecipesByBasicIngredientId(Long basicIngredientId){

        return recipeRepository.findByBasicIngredientId(basicIngredientId);
    }
}
