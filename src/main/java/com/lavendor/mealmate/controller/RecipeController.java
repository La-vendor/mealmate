package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();

        if (recipes != null && !recipes.isEmpty()) {
            return ResponseEntity.ok(recipes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable("id") String stringId) {

        Long recipeId = Long.valueOf(stringId);
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return ResponseEntity.ok(recipe);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") String stringId){
        Long recipeId = Long.valueOf(stringId);
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.ok("Recipe with ID: " + recipeId + " deleted successfully");
    }
}
