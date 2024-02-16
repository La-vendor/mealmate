package com.lavendor.mealmate.ingredient;

import com.lavendor.mealmate.ingredient.BasicIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasicIngredientRepository extends JpaRepository<BasicIngredient, Long> {

    Optional<BasicIngredient> findByBasicIngredientNameIgnoreCase(String basicIngredientName);
    boolean existsByBasicIngredientName(String basicIngredientName);

    List<BasicIngredient> findByUserId(Long userId);

    Optional<BasicIngredient> findByBasicIngredientNameAndUserId(String basicIngredientName, Long userId);

}
