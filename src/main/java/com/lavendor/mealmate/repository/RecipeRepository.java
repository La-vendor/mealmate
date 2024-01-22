package com.lavendor.mealmate.repository;

import com.lavendor.mealmate.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByRecipeName(String recipeName);

    List<Recipe> findAllByUserId(Long userId);

    @Query("SELECT r FROM Recipe r JOIN r.recipeIngredients ri WHERE ri.basicIngredient.basicIngredientId = :basicIngredientId")
    List<Recipe> findByBasicIngredientId(@Param("basicIngredientId") Long basicIngredientId);


    Optional<Recipe> findByRecipeNameAndUserId(String recipeName, Long userId);

}
