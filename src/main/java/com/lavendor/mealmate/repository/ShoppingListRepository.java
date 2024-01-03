package com.lavendor.mealmate.repository;

import com.lavendor.mealmate.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
}
