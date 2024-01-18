package com.lavendor.mealmate.model;

import jakarta.persistence.*;

@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_ingredient_id")
    private Long recipeIngredientId;

    @ManyToOne
    @JoinColumn(name = "basic_ingredient_id")
    private BasicIngredient basicIngredient;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private double quantity;

    public RecipeIngredient() {
    }

    public RecipeIngredient(BasicIngredient basicIngredient, double quantity, Recipe recipe) {
        this.basicIngredient = basicIngredient;
        this.quantity = quantity;
        this.recipe = recipe;
    }

    public RecipeIngredient(BasicIngredient basicIngredient, double quantity) {
        this.basicIngredient = basicIngredient;
        this.quantity = quantity;
    }

    public RecipeIngredient(RecipeIngredient recipeIngredient) {
        this.basicIngredient = recipeIngredient.getBasicIngredient();
        this.quantity = recipeIngredient.getQuantity();
    }

    public Long getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public void setRecipeIngredientId(Long recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
    }

    public BasicIngredient getBasicIngredient() {
        return basicIngredient;
    }

    public void setBasicIngredient(BasicIngredient basicIngredient) {
        this.basicIngredient = basicIngredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }


}
