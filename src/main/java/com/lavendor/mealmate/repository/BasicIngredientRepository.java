package com.lavendor.mealmate.repository;

import com.lavendor.mealmate.model.BasicIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasicIngredientRepository extends JpaRepository<BasicIngredient, Long> {

    Optional<BasicIngredient> findByBasicIngredientName(String basicIngredientName);
}
