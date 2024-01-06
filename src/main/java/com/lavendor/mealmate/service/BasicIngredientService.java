package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.repository.BasicIngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicIngredientService {

    private final BasicIngredientRepository basicIngredientRepository;

    public BasicIngredientService(BasicIngredientRepository basicIngredientRepository) {
        this.basicIngredientRepository = basicIngredientRepository;
    }

    public BasicIngredient addBasicIngredient(String basicIngredientName, String unit){
        BasicIngredient basicIngredient = new BasicIngredient(basicIngredientName, unit);
        return basicIngredientRepository.save(basicIngredient);
    }

    public BasicIngredient getBasicIngredientById(Long basicIngredientId){
        return basicIngredientRepository.findById(basicIngredientId).orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
    }

    public BasicIngredient getBasicIngredientByName(String basicIngredientName){
        return basicIngredientRepository.findByBasicIngredientNameIgnoreCase(basicIngredientName).orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
    }

    public List<BasicIngredient> getAllBasicIngredients(){
        return basicIngredientRepository.findAll();
    }

    public void deleteBasicIngredient(Long basicIngredientId){
        BasicIngredient basicIngredient = basicIngredientRepository.findById(basicIngredientId).orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
        basicIngredientRepository.delete(basicIngredient);
    }

}
