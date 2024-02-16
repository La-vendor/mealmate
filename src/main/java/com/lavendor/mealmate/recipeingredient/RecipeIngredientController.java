package com.lavendor.mealmate.recipeingredient;

import com.lavendor.mealmate.recipeingredient.RecipeIngredient;
import com.lavendor.mealmate.recipeingredient.RecipeIngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/recipe-ingredient")
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    public RecipeIngredientController(RecipeIngredientService recipeIngredientService) {
        this.recipeIngredientService = recipeIngredientService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<RecipeIngredient>> getAllRecipeIngredients() {
        List<RecipeIngredient> recipeIngredients = recipeIngredientService.getAllRecipeIngredients();

        if (recipeIngredients != null && !recipeIngredients.isEmpty()) {
            return ResponseEntity.ok(recipeIngredients);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<RecipeIngredient> getRecipeIngredientById(@PathVariable("id") String stringId) {

        Long recipeIngredientId = Long.valueOf(stringId);
        RecipeIngredient recipeIngredient = recipeIngredientService.getRecipeIngredientById(recipeIngredientId);
        return ResponseEntity.ok(recipeIngredient);
    }
}

