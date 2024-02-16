package com.lavendor.mealmate.service;

import com.lavendor.mealmate.MealmateApplication;
import com.lavendor.mealmate.dailymenu.DailyMenu;
import com.lavendor.mealmate.ingredient.BasicIngredient;
import com.lavendor.mealmate.recipe.Recipe;
import com.lavendor.mealmate.recipeingredient.RecipeIngredient;
import com.lavendor.mealmate.shoppinglist.ShoppingList;
import com.lavendor.mealmate.shoppinglist.ShoppingListRepository;
import com.lavendor.mealmate.shoppinglist.ShoppingListService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = MealmateApplication.class)
public class ShoppingListServiceTest {

    private final Long userId = 100L;
    private final Long shoppingListId  = 100L;
    String recipeName = "Recipe name";


    private List<DailyMenu> dailyMenus;
    private List<DailyMenu> dailyMenusWithoutRecipes;

    private BasicIngredient basicIngredient1;
    private BasicIngredient basicIngredient2;
    private BasicIngredient basicIngredient3;

    @MockBean
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ShoppingListService shoppingListService;

    @BeforeEach
    void init() {

        LocalDate date1 = LocalDate.now();
        LocalDate date2 = date1.plusDays(1);

        DailyMenu dailyMenu1 = new DailyMenu(date1, userId);
        DailyMenu dailyMenu2 = new DailyMenu(date2, userId);
        DailyMenu dailyMenu3 = new DailyMenu(date1, userId);

        basicIngredient1 = new BasicIngredient("ingredient1");
        basicIngredient2 = new BasicIngredient("ingredient2");
        basicIngredient3 = new BasicIngredient("ingredient3");

        RecipeIngredient recipeIngredient1 = new RecipeIngredient(basicIngredient1, 100.0);
        RecipeIngredient recipeIngredient2 = new RecipeIngredient(basicIngredient2, 30.0);
        RecipeIngredient recipeIngredient3 = new RecipeIngredient(basicIngredient3, 20.0);

        List<RecipeIngredient> recipeIngredients1 = Arrays.asList(recipeIngredient1, recipeIngredient2);
        List<RecipeIngredient> recipeIngredients2 = Arrays.asList(recipeIngredient2, recipeIngredient3);
        Recipe recipe1 = new Recipe(recipeName, recipeIngredients1, userId);
        Recipe recipe2 = new Recipe(recipeName, recipeIngredients2, userId);

        List<Recipe> recipeList1 = List.of(recipe1);
        List<Recipe> recipeList2 = List.of(recipe2);

        dailyMenu1.setRecipeList(recipeList1);
        dailyMenu2.setRecipeList(recipeList2);

        dailyMenus = Arrays.asList(dailyMenu1, dailyMenu2);
        dailyMenusWithoutRecipes = List.of(dailyMenu3);
    }

    @Test
    public void testGenerateNewShoppingList() {

        when(shoppingListRepository.findByUserId(userId)).thenReturn(Optional.empty());

        when(shoppingListRepository.save(any(ShoppingList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShoppingList result = shoppingListService.generateOrUpdateShoppingList(userId, dailyMenus);

        assertNotNull(result);
        assertThat(result.getIngredientQuantityMap(), hasEntry(basicIngredient1, 100.0));
        assertThat(result.getIngredientQuantityMap(), hasEntry(basicIngredient2, 60.0));
        assertThat(result.getIngredientQuantityMap(), hasEntry(basicIngredient3, 20.0));
        assertEquals(userId, result.getUserId());
    }

    @Test
    public void testGenerateShoppingListWithoutIngredients(){

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            shoppingListService.generateOrUpdateShoppingList(userId, dailyMenusWithoutRecipes);
        });
        assertTrue(exception.getMessage().contains("No RecipeIngredients found from DailyMenus"));
    }

    @Test
    public void testAddIngredientsToShoppingList() {
        Map<BasicIngredient, Double> shoppingListEntries = new HashMap<>();
        String ingredientName = "New Ingredient";
        Double ingredientQuantity = 10.0;
        BasicIngredient basicIngredientToAdd = new BasicIngredient(ingredientName);

        shoppingListEntries.put(basicIngredientToAdd,ingredientQuantity);

        when(shoppingListRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(shoppingListRepository.save(any(ShoppingList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShoppingList generatedOrUpdateShoppingList = shoppingListService.generateOrUpdateShoppingList(userId, dailyMenus);

        when(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(generatedOrUpdateShoppingList));

        ShoppingList result = shoppingListService.addIngredientsToShoppingList(shoppingListId, shoppingListEntries);

        assertNotNull(result);
        assertThat(result.getIngredientQuantityMap(), hasEntry(basicIngredientToAdd, 10.0));
        assertEquals(userId, result.getUserId());
    }

    @Test
    public void testGetShoppingListById() {

        when(shoppingListRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(shoppingListRepository.save(any(ShoppingList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShoppingList generatedOrUpdateShoppingList = shoppingListService.generateOrUpdateShoppingList(userId, dailyMenus);

        when(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(generatedOrUpdateShoppingList));

        ShoppingList resultShoppingList = shoppingListService.getShoppingListById(shoppingListId);

        assertEquals(generatedOrUpdateShoppingList,resultShoppingList);
    }

    @Test
    public void testGetShoppingMapById_NonExistingEntity(){

        when(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            shoppingListService.getShoppingListById(shoppingListId);
        });
        assertTrue(exception.getMessage().contains("Shopping List not found"));
    }

    @Test
    public void testGetShoppingListByUserId() {

        when(shoppingListRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(shoppingListRepository.save(any(ShoppingList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShoppingList generatedOrUpdateShoppingList = shoppingListService.generateOrUpdateShoppingList(userId, dailyMenus);

        when(shoppingListRepository.findByUserId(userId)).thenReturn(Optional.of(generatedOrUpdateShoppingList));

        Map<BasicIngredient, Double> result = shoppingListService.getShoppingListByUserId(userId);

        assertNotNull(result);
        assertEquals(generatedOrUpdateShoppingList.getIngredientQuantityMap(),result);
    }

    @Test
    public void testGetShoppingListByUserId_NonExistingEntity(){

        when(shoppingListRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Map<BasicIngredient, Double> result = shoppingListService.getShoppingListByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
