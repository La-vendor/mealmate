package com.lavendor.mealmate.service;

import com.lavendor.mealmate.MealmateApplication;
import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.model.RecipeIngredient;
import com.lavendor.mealmate.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MealmateApplication.class)
public class RecipeServiceTest {

    List<RecipeIngredient> recipeIngredients;

    @MockBean
    RecipeRepository recipeRepository;

    @Autowired
    RecipeService recipeService;

    @BeforeEach
    void init(){
        BasicIngredient basicIngredient1 = new BasicIngredient("ingredient1");
        BasicIngredient basicIngredient2 = new BasicIngredient("ingredient2");

        RecipeIngredient recipeIngredient1 = new RecipeIngredient(basicIngredient1,100.0);
        RecipeIngredient recipeIngredient2 = new RecipeIngredient(basicIngredient2,50.0);

        recipeIngredients= Arrays.asList(recipeIngredient1, recipeIngredient2);
    }

    @Test
    public void createRecipe(){
        Long userId = 1L;
        String recipeName = "Recipe name";

        Recipe expectedRecipe = new Recipe(recipeName, recipeIngredients, userId);

        when(recipeRepository.save(any(Recipe.class))).thenReturn(expectedRecipe);

        Recipe result = recipeService.createRecipe(recipeName,recipeIngredients,userId);

        verify(recipeRepository,timeout(1)).save(any(Recipe.class));
        assertEquals(expectedRecipe, result);
    }

    @Test
    public void testGetRecipeById() {

        Long recipeId = 1L;
        Long userId = 1L;
        String recipeName = "Recipe name";

        Recipe expectedRecipe = new Recipe(recipeName, recipeIngredients, userId);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(expectedRecipe));

        Recipe result = recipeService.getRecipeById(recipeId);

        assertEquals(expectedRecipe, result);
    }

    @Test
    public void testGetRecipeById_NonExistingEntity() {
        Long recipeId = 1L;

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            recipeService.getRecipeById(recipeId);
        });
    }

    @Test
    public void testGetAllRecipes() {
        Long userId = 1L;

        Recipe mockRecipe1 = new Recipe("Recipe name 1", recipeIngredients, userId);
        Recipe mockRecipe2 = new Recipe("Recipe name 2", recipeIngredients, userId);

        List<Recipe> expectedRecipeList = Arrays.asList(mockRecipe1, mockRecipe2);

        when(recipeRepository.findAll()).thenReturn(expectedRecipeList);

        List<Recipe> result = recipeService.getAllRecipes();

        verify(recipeRepository, times(1)).findAll();
        assertEquals(expectedRecipeList, result);
    }

    @Test
    public void testDeleteRecipe() {
        Long recipeId = 1L;
        Long userId = 1L;
        String recipeName = "Recipe name";

        Recipe expectedRecipe = new Recipe(recipeName, recipeIngredients, userId);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(expectedRecipe));

        recipeService.deleteRecipe(recipeId);

        verify(recipeRepository, times(1)).delete(expectedRecipe);
    }

    @Test
    public void testDeleteRecipe_NonExistingEntity() {
        Long recipeId = 1L;

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            recipeService.deleteRecipe(recipeId);
        });
    }

    @Test
    public void testGetUserRecipes(){
        Long userId = 1L;

        Recipe mockRecipe1 = new Recipe("Recipe name 1", recipeIngredients, userId);
        Recipe mockRecipe2 = new Recipe("Recipe name 2", recipeIngredients, userId);

        List<Recipe> expectedRecipeList = Arrays.asList(mockRecipe1, mockRecipe2);

        when(recipeRepository.findAllByUserId(userId)).thenReturn(expectedRecipeList);

        List<Recipe> result = recipeService.getRecipesByUserId(userId);

        verify(recipeRepository, times(1)).findAllByUserId(userId);
        assertEquals(expectedRecipeList,result);
    }
}
