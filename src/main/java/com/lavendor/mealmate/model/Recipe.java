package com.lavendor.mealmate.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;
    private String recipeName;


    @JoinColumn(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<RecipeIngredient> recipeIngredients;


    public Recipe() {
    }

    public Recipe(Recipe recipe) {
        this.recipeName = recipe.getRecipeName();
        this.recipeIngredients = recipe.getIngredients();
    }

    public Recipe(String recipeName, List<RecipeIngredient> recipeIngredients, Long userId) {
        this.recipeName = recipeName;
        this.recipeIngredients = recipeIngredients;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<RecipeIngredient> getIngredients() {
        return recipeIngredients;
    }

    public void setIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public void addIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.add(recipeIngredient);
    }
}
