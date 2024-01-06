package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.*;
import com.lavendor.mealmate.repository.BasicIngredientRepository;
import com.lavendor.mealmate.repository.ShoppingListRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final BasicIngredientRepository basicIngredientRepository;


    public ShoppingListService(ShoppingListRepository shoppingListRepository, BasicIngredientRepository basicIngredientRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.basicIngredientRepository = basicIngredientRepository;
    }

    public ShoppingList generateShoppingList(String name, List<DailyMenu> dailyMenus) {
        List<RecipeIngredient> recipeIngredients = extractRecipeIngredientsFromDailyMenus(dailyMenus);

        if (recipeIngredients.isEmpty()) {
            throw new RuntimeException("No RecipeIngredients found from DailyMenus");
        }
        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByName(name);

        if (optionalShoppingList.isPresent()) {
            ShoppingList shoppingList = optionalShoppingList.get();
            shoppingList.setRecipeIngredients(recipeIngredients);
            return shoppingListRepository.save(shoppingList);
        } else {
            ShoppingList newShoppingList = new ShoppingList(name, recipeIngredients);
            return shoppingListRepository.save(newShoppingList);
        }
    }

    public ShoppingList addIngredientsToShoppingList(Long shoppingListId, List<RecipeIngredient> recipeIngredients) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new EntityNotFoundException("Shopping List not found"));

        shoppingList.addRecipeIngredients(recipeIngredients);
        return shoppingListRepository.save(shoppingList);
    }

    public List<RecipeIngredient> getRecipeIngredientsForShoppingList(Long shoppingListId) {
        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findById(shoppingListId);
        if (optionalShoppingList.isPresent()) {
            ShoppingList shoppingList = optionalShoppingList.get();
            return shoppingList.getRecipeIngredients();
        } else {
            return Collections.emptyList();
        }
    }

    public List<RecipeIngredient> getMergedShoppingList(Long shoppingListId) {
        List<RecipeIngredient> recipeIngredients;
        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findById(shoppingListId);
        if (optionalShoppingList.isPresent()) {
            ShoppingList shoppingList = optionalShoppingList.get();
            recipeIngredients = shoppingList.getRecipeIngredients();
            return aggregateIngredients(recipeIngredients);
        }
        return Collections.emptyList();
    }

    private List<RecipeIngredient> aggregateIngredients(List<RecipeIngredient> recipeIngredients){
        Map<String, Double> ingredientQuantityMap = new HashMap<>();
        List<RecipeIngredient> aggregatedIngredients = new ArrayList<>();

        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            String key = recipeIngredient.getBasicIngredient().getBasicIngredientName();
            double currentQuantity = ingredientQuantityMap.getOrDefault(key, 0.0);
            double newQuantity = currentQuantity + recipeIngredient.getQuantity();
            ingredientQuantityMap.put(key, newQuantity);
        }

        for (Map.Entry<String, Double> entry : ingredientQuantityMap.entrySet()) {
            String ingredientName = entry.getKey();
            double newQuantity = entry.getValue();
            Optional<BasicIngredient> optionalBasicIngredient = basicIngredientRepository.findByBasicIngredientNameIgnoreCase(ingredientName);
            if(optionalBasicIngredient.isPresent()){
                BasicIngredient basicIngredient = optionalBasicIngredient.get();
                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setBasicIngredient(basicIngredient);
                recipeIngredient.setQuantity(newQuantity);
                aggregatedIngredients.add(recipeIngredient);
            }
        }
        return aggregatedIngredients;
    }

//    private List<RecipeIngredient> aggregateIngredients(Map<RecipeIngredient, Double> ingredientQuantityMap) {
//        List<RecipeIngredient> aggregatedIngredients = new ArrayList<>();
//        for (Map.Entry<RecipeIngredient, Double> entry : ingredientQuantityMap.entrySet()) {
//            RecipeIngredient recipeIngredient = entry.getKey();
//            double newQuantity = entry.getValue();
//            recipeIngredient.setQuantity(newQuantity);
//            aggregatedIngredients.add(recipeIngredient);
//        }
//        return aggregatedIngredients;
//    }

    public List<ShoppingList> getAllShoppingLists() {
        return shoppingListRepository.findAll();
    }

    public void deleteShoppingList(Long shoppingListId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new EntityNotFoundException("Shopping List not found"));
        shoppingListRepository.delete(shoppingList);
    }

    private List<RecipeIngredient> extractRecipeIngredientsFromDailyMenus(List<DailyMenu> dailyMenus) {
        List<RecipeIngredient> extractedIngredients = new ArrayList<>();
        for (DailyMenu menu : dailyMenus) {
            List<Recipe> recipes = menu.getRecipeList();
            if (recipes != null) {
                for (Recipe recipe : recipes) {
                    List<RecipeIngredient> recipeIngredients = recipe.getIngredients();
                    if (recipeIngredients != null) {
                        extractedIngredients.addAll(recipeIngredients);
                    }
                }
            }
        }
        return extractedIngredients;
    }

}
