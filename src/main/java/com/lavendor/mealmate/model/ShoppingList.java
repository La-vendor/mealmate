package com.lavendor.mealmate.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "shopping_lists")
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingListId;
    private String name;
    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "shopping_list_ingredients",
            joinColumns = @JoinColumn(name = "shopping_list_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<RecipeIngredient> recipeIngredients;


}
