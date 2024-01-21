package com.lavendor.mealmate.exception;

public class IngredientDeleteException extends RuntimeException {

    public IngredientDeleteException(String ingredientName) {
        super("Cannot delete ingredient '" + ingredientName + "'. It is still used in one or more recipes.");
    }
}
