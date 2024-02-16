package com.lavendor.mealmate.recipeingredient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    List<RecipeIngredient> findAllByRecipe_RecipeId(Long recipeId);
}
