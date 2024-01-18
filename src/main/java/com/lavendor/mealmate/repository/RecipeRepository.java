package com.lavendor.mealmate.repository;

import com.lavendor.mealmate.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByRecipeName(String recipeName);

    List<Recipe> findAllByUserId(Long userId);
}
