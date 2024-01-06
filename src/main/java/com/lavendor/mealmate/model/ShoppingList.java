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

    public ShoppingList() {
    }

    public ShoppingList(String name, List<RecipeIngredient> recipeIngredients){
        this.name = name;
        this.recipeIngredients = recipeIngredients;
    }

    public void addRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients.addAll(recipeIngredients);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
}
