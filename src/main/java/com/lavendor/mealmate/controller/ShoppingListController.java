package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.model.DailyMenu;
import com.lavendor.mealmate.model.ShoppingList;
import com.lavendor.mealmate.service.DailyMenuService;
import com.lavendor.mealmate.service.ShoppingListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final DailyMenuService dailyMenuService;

    public ShoppingListController(ShoppingListService shoppingListService, DailyMenuService dailyMenuService) {
        this.shoppingListService = shoppingListService;
        this.dailyMenuService = dailyMenuService;
    }

    @GetMapping()
    public String getRecipePage(Model model){
        Map<BasicIngredient,Double> ingredientQuantityMap = shoppingListService.getShoppingListMap(1L);

        Map<BasicIngredient, Double> sortedIngredientQuantityMap = ingredientQuantityMap.entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getBasicIngredientName(), String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        model.addAttribute("ingredientQuantityMap", sortedIngredientQuantityMap);
        model.addAttribute("currentPage", "shopping-list");
        return "shopping-list";
    }


    @GetMapping("/generate")
    public String generateShoppingList(){
        List<DailyMenu> dailyMenus = dailyMenuService.getAllDailyMenu();
        ShoppingList shoppingList = shoppingListService.generateOrUpdateShoppingList("temp",dailyMenus );

        return "redirect:/shopping-list";
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Map<BasicIngredient, Double>> getElementsFromShoppingList(@PathVariable("id") Long shoppingListId){
        Map<BasicIngredient, Double> shoppingListElements = shoppingListService.getShoppingListMap(shoppingListId);
        if(shoppingListElements!=null){
            return ResponseEntity.ok(shoppingListElements);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyMap());
        }
    }
//    @GetMapping("/merged/{id}")
//    public ResponseEntity<List<RecipeIngredient>> getMergedShoppingList(@PathVariable("id") Long shoppingListId){
//        List<ShoppingList> shoppingLists = shoppingListService.getAllShoppingLists();
//
//        List<RecipeIngredient> mergedIngredients = shoppingListService.getMergedShoppingList(shoppingListId);
//
//        return ResponseEntity.ok(mergedIngredients);
//    }

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
