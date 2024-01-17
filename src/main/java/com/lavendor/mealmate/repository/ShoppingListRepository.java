package com.lavendor.mealmate.repository;

import com.lavendor.mealmate.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    Optional<ShoppingList> findByUserId(Long userId);

}
