package com.lavendor.mealmate.repository;

import com.lavendor.mealmate.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByIngredientName(String ingredientName);
}
