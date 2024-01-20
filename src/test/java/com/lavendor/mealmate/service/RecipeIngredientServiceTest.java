package com.lavendor.mealmate.service;

import com.lavendor.mealmate.MealmateApplication;
import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.model.RecipeIngredient;
import com.lavendor.mealmate.repository.RecipeIngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest(classes = MealmateApplication.class)
public class RecipeIngredientServiceTest {

    BasicIngredient basicIngredient1;
    BasicIngredient basicIngredient2;

    Long ingredientId = 100L;

    @MockBean
    RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    RecipeIngredientService recipeIngredientService;

    @BeforeEach
    void init() {
        basicIngredient1 = new BasicIngredient("ingredient1");
        basicIngredient2 = new BasicIngredient("ingredient2");
    }

    @Test
    public void testAddRecipeIngredient() {
        double quantity = 50.0;
        Recipe recipe = new Recipe();

        RecipeIngredient expectedRecipeIngredient = new RecipeIngredient(basicIngredient1, quantity, recipe);

        when(recipeIngredientRepository.save(any(RecipeIngredient.class))).thenReturn(expectedRecipeIngredient);

        RecipeIngredient result = recipeIngredientService.addRecipeIngredient(basicIngredient1, quantity, recipe);

        assertEquals(expectedRecipeIngredient, result);
    }

    @Test
    public void testGetRecipeIngredientById() {
        double quantity = 50.0;
        Recipe recipe = new Recipe();

        RecipeIngredient expectedRecipeIngredient = new RecipeIngredient(basicIngredient1, quantity, recipe);

        when(recipeIngredientRepository.findById(ingredientId)).thenReturn(Optional.of(expectedRecipeIngredient));

        RecipeIngredient result = recipeIngredientService.getRecipeIngredientById(ingredientId);

        assertEquals(expectedRecipeIngredient, result);
    }

    @Test
    public void testGetRecipeIngredientById_NonExistingEntity() {

        when(recipeIngredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            recipeIngredientService.getRecipeIngredientById(ingredientId);
        });
    }

    @Test
    public void testGetAllRecipeIngredients() {

        RecipeIngredient mockRecipeIngredient1 = new RecipeIngredient(basicIngredient1, 100.0);
        RecipeIngredient mockRecipeIngredient2 = new RecipeIngredient(basicIngredient2, 50.0);

        List<RecipeIngredient> expectedIngredientList = Arrays.asList(mockRecipeIngredient1, mockRecipeIngredient2);

        when(recipeIngredientRepository.findAll()).thenReturn(expectedIngredientList);

        List<RecipeIngredient> result = recipeIngredientService.getAllRecipeIngredients();

        assertEquals(expectedIngredientList, result);
    }

    @Test
    public void testDeleteRecipeIngredient() {
        RecipeIngredient mockRecipeIngredient = new RecipeIngredient(basicIngredient1, 100.0);

        when(recipeIngredientRepository.findById(ingredientId)).thenReturn(Optional.of(mockRecipeIngredient));

        recipeIngredientService.deleteRecipeIngredient(ingredientId);

        verify(recipeIngredientRepository, times(1)).delete(mockRecipeIngredient);
    }

    @Test
    public void testDeleteRecipeIngredient_NonExistingEntity() {

        when(recipeIngredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            recipeIngredientService.deleteRecipeIngredient(ingredientId);
        });
    }

    @Test
    public void testAddRecipeToIngredients() {
        Recipe recipe = new Recipe();

        RecipeIngredient mockRecipeIngredient1 = new RecipeIngredient(basicIngredient1, 100.0);
        RecipeIngredient mockRecipeIngredient2 = new RecipeIngredient(basicIngredient2, 50.0);

        List<RecipeIngredient> recipeIngredientList = Arrays.asList(mockRecipeIngredient1, mockRecipeIngredient2);

        recipeIngredientService.addRecipeToIngredients(recipeIngredientList, recipe);

        verify(recipeIngredientRepository, times(1)).saveAll(recipeIngredientList);
    }
}
