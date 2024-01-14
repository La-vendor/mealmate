package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.model.RecipeIngredient;
import com.lavendor.mealmate.service.BasicIngredientService;
import com.lavendor.mealmate.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//TODO Make so that every element on recipe list is a button that takes you to list of ingredients of that recipe
@Controller
@RequestMapping("/recipe")
@SessionAttributes({"temporaryIngredients", "recipeIngredients"})
public class RecipeController {

    private final RecipeService recipeService;
    private final BasicIngredientService basicIngredientService;

    @ModelAttribute("temporaryIngredients")
    public List<RecipeIngredient> temporaryIngredients() {
        return new ArrayList<>();
    }

    @ModelAttribute("recipeIngredients")
    public List<RecipeIngredient> recipeIngredients() {
        return new ArrayList<>();
    }

    public RecipeController(RecipeService recipeService, BasicIngredientService basicIngredientService) {
        this.recipeService = recipeService;
        this.basicIngredientService = basicIngredientService;
    }


    @GetMapping()
    public String getRecipePage(@ModelAttribute("temporaryIngredients") List<RecipeIngredient> temporaryIngredients,
                                @ModelAttribute("recipeIngredients") List<RecipeIngredient> recipeIngredients,
                                Model model) {
        List<Recipe> recipeList = recipeService.getAllRecipes();
        List<BasicIngredient> basicIngredientList = basicIngredientService.getAllBasicIngredients();
        basicIngredientList.sort(Comparator.comparing(BasicIngredient::getBasicIngredientName, String.CASE_INSENSITIVE_ORDER));

        model.addAttribute("recipeIngredients", recipeIngredients);
        model.addAttribute("temporaryIngredients", temporaryIngredients);
        model.addAttribute("basicIngredientList", basicIngredientList);
        model.addAttribute("recipeList", recipeList);
        model.addAttribute("currentPage", "recipe");
        return "recipe";
    }


    @PostMapping("/add-ingredient")
    public String addIngredientToRecipe(@RequestParam("selectedIngredientId") Long selectedIngredientId,
                                        @RequestParam("newIngredientQuantity") Double newIngredientQuantity,
                                        @SessionAttribute("temporaryIngredients") List<RecipeIngredient> temporaryIngredients) {
        RecipeIngredient selectedIngredient = new RecipeIngredient(basicIngredientService.getBasicIngredientById(selectedIngredientId), newIngredientQuantity);

        temporaryIngredients.add(selectedIngredient);
        return "redirect:/recipe";
    }

    @GetMapping("/reset")
    public String deleteIngredient(@SessionAttribute("temporaryIngredients") List<RecipeIngredient> temporaryIngredients){
        temporaryIngredients.clear();
        return "redirect:/recipe";
    }

    @PostMapping("/add")
    public String addRecipe(@RequestParam("newRecipeName") String newRecipeName,
                            @SessionAttribute("temporaryIngredients") List<RecipeIngredient> temporaryIngredients) {

        Recipe recipe = recipeService.createRecipe(newRecipeName, temporaryIngredients);
        temporaryIngredients.clear();
        return "redirect:/recipe";
    }

    @GetMapping("/ingredients/{recipeId}")
    public String displayRecipeIngredient(@PathVariable("recipeId") Long recipeId,
                                          @SessionAttribute("recipeIngredients") List<RecipeIngredient> recipeIngredients){
        recipeIngredients.clear();
        recipeIngredients.addAll(recipeService.getRecipeById(recipeId).getIngredients());
        return "redirect:/recipe";
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
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") String stringId) {
        Long recipeId = Long.valueOf(stringId);
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.ok("Recipe with ID: " + recipeId + " deleted successfully");
    }
}
