package com.lavendor.mealmate.repository;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}
