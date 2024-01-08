package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.model.DailyMenu;
import com.lavendor.mealmate.model.RecipeIngredient;
import com.lavendor.mealmate.model.ShoppingList;
import com.lavendor.mealmate.service.DailyMenuService;
import com.lavendor.mealmate.service.ShoppingListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final DailyMenuService dailyMenuService;

    public ShoppingListController(ShoppingListService shoppingListService, DailyMenuService dailyMenuService) {
        this.shoppingListService = shoppingListService;
        this.dailyMenuService = dailyMenuService;
    }

    @GetMapping("/generate")
    public ResponseEntity<?> generateShoppingList(){
        List<DailyMenu> dailyMenus = dailyMenuService.getAllDailyMenu();

        try{
            ShoppingList shoppingList = shoppingListService.generateShoppingList("temp",dailyMenus );
            return ResponseEntity.ok(shoppingList);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shopping list generation failed: " + e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<List<RecipeIngredient>> getRecipeIngredientsFromShoppingList(@PathVariable("id") Long shoppingListId){
        List<RecipeIngredient> recipeIngredients = shoppingListService.getRecipeIngredientsForShoppingList(shoppingListId);
        if(recipeIngredients!=null){
            return ResponseEntity.ok(recipeIngredients);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }
    @GetMapping("/merged/{id}")
    public ResponseEntity<List<RecipeIngredient>> getMergedShoppingList(@PathVariable("id") Long shoppingListId){
        List<ShoppingList> shoppingLists = shoppingListService.getAllShoppingLists();

        List<RecipeIngredient> mergedIngredients = shoppingListService.getMergedShoppingList(shoppingListId);

        return ResponseEntity.ok(mergedIngredients);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ShoppingList>> getAllShoppingLists(){
        List<ShoppingList> shoppingLists = shoppingListService.getAllShoppingLists();

        if(shoppingLists != null && !shoppingLists.isEmpty()){
            return ResponseEntity.ok(shoppingLists);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }
}
