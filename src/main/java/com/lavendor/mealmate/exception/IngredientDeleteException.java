package com.lavendor.mealmate.exception;

import com.lavendor.mealmate.recipe.Recipe;

import java.util.List;

public class IngredientDeleteException extends RuntimeException {

    public IngredientDeleteException(String ingredientName, List<Recipe> recipeList) {
        super("Cannot delete ingredient '" + ingredientName + "'. It is still used in recipes: " + recipeList.toString());
    }
}
