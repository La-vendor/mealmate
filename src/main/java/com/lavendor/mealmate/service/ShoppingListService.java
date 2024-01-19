package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.*;
import com.lavendor.mealmate.repository.ShoppingListRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;


    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public ShoppingList generateOrUpdateShoppingList(Long userId, List<DailyMenu> dailyMenus) {
        List<RecipeIngredient> recipeIngredients = extractRecipeIngredientsFromDailyMenus(dailyMenus);

        if (recipeIngredients.isEmpty()) {
            throw new RuntimeException("No RecipeIngredients found from DailyMenus");
        }
        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(userId);

        ShoppingList shoppingList = optionalShoppingList.orElseGet(() -> new ShoppingList(userId, new HashMap<>()));

        shoppingList.setIngredientQuantityMap(mergeIngredients(new HashMap<>(), recipeIngredients));
        shoppingList.setUserId(userId);
        return shoppingListRepository.save(shoppingList);
    }

    public ShoppingList addIngredientsToShoppingList(Long shoppingListId, Map<BasicIngredient, Double> recipeIngredients) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new EntityNotFoundException("Shopping List not found"));

        shoppingList.addShoppingListElement(recipeIngredients);
        return shoppingListRepository.save(shoppingList);
    }

    public Map<BasicIngredient, Double> getShoppingListByUserId(Long userId) {
        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(userId);
        if (optionalShoppingList.isPresent()) {
            ShoppingList shoppingList = optionalShoppingList.get();
            return shoppingList.getIngredientQuantityMap();
        } else {
            return Collections.emptyMap();
        }
    }

    public ShoppingList getShoppingListById(Long shoppingListId) {
        return shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new EntityNotFoundException("Shopping List not found"));
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

    private Map<BasicIngredient, Double> mergeIngredients(Map<BasicIngredient, Double> existingIngredients,
                                                          List<RecipeIngredient> newRecipeIngredients) {
        for (RecipeIngredient recipeIngredient : newRecipeIngredients) {
            BasicIngredient basicIngredient = recipeIngredient.getBasicIngredient();
            double quantity = recipeIngredient.getQuantity();

            existingIngredients.merge(basicIngredient, quantity, Double::sum);
        }
        return existingIngredients;
    }
}
