package com.lavendor.mealmate.service;

import com.lavendor.mealmate.MealmateApplication;
import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.repository.BasicIngredientRepository;
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
public class BasicIngredientServiceTest {

    String testIngredientName = "Ingredient";
    String testIngredientUnit = "grams";
    Long activeUserId;

    @MockBean
    private BasicIngredientRepository basicIngredientRepository;

    @Autowired
    private BasicIngredientService basicIngredientService;

    @Autowired
    UserService userService;

    @BeforeEach
    public void init() {
        testIngredientName = "Minced Meat";
        testIngredientUnit = "grams";
        activeUserId = 1L;
    }

    @Test
    public void testAddBasicIngredient() {

        BasicIngredient expectedIngredient = new BasicIngredient(testIngredientName, testIngredientUnit, activeUserId);

        when(basicIngredientRepository.save(any(BasicIngredient.class))).thenReturn(expectedIngredient);

        BasicIngredient result = basicIngredientService.addBasicIngredient(testIngredientName, testIngredientUnit, activeUserId);

        assertEquals(expectedIngredient, result);
        verify(basicIngredientRepository, times(1)).save(any(BasicIngredient.class));
    }

    @Test
    public void testGetBasicIngredientById() {
        Long ingredientId = 1L;
        BasicIngredient expectedIngredient = new BasicIngredient(testIngredientName, testIngredientUnit, activeUserId);

        when(basicIngredientRepository.findById(ingredientId)).thenReturn(Optional.of(expectedIngredient));

        BasicIngredient result = basicIngredientService.getBasicIngredientById(ingredientId);

        assertNotNull(result);
        assertEquals(expectedIngredient, result);
    }

    @Test
    void testGetBasicIngredientById_NonExistingEntity() {
        Long ingredientId = 1L;

        when(basicIngredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> basicIngredientService.getBasicIngredientById(ingredientId));
    }

    @Test
    void testCheckIfIngredientExists() {
        String ingredientName = "Ingredient Name";

        when(basicIngredientRepository.existsByBasicIngredientName(ingredientName)).thenReturn(true);

        assertTrue(basicIngredientService.checkIfIngredientExists(ingredientName));
    }

    @Test
    public void testGetAllBasicIngredients() {

        BasicIngredient ingredient1 = new BasicIngredient("Item1");
        BasicIngredient ingredient2 = new BasicIngredient("Item2");

        List<BasicIngredient> mockedList = Arrays.asList(ingredient1, ingredient2);

        when(basicIngredientRepository.findAll()).thenReturn(mockedList);

        List<BasicIngredient> result = basicIngredientService.getAllBasicIngredients();

        verify(basicIngredientRepository, times(1)).findAll();

        assertEquals(mockedList, result);
    }

    @Test
    public void testDeleteBasicIngredient() {
        Long basicIngredientId = 1L;
        BasicIngredient mockIngredient = new BasicIngredient("Item1");

        when(basicIngredientRepository.findById(basicIngredientId)).thenReturn(Optional.of(mockIngredient));

        basicIngredientService.deleteBasicIngredient(basicIngredientId);

        verify(basicIngredientRepository, times(1)).findById(basicIngredientId);
        verify(basicIngredientRepository, times(1)).delete(mockIngredient);
    }

    @Test
    public void testGetAllUserBasicIngredients() {
        Long userId = 1L;

        BasicIngredient ingredient1 = new BasicIngredient("Item1", "grams", userId);
        BasicIngredient ingredient2 = new BasicIngredient("Item2", "grams", userId);

        List<BasicIngredient> mockedList = Arrays.asList(ingredient1, ingredient2);

        when(basicIngredientRepository.findByUserId(userId)).thenReturn(mockedList);

        List<BasicIngredient> result = basicIngredientService.getBasicIngredientsByUserId(userId);

        verify(basicIngredientRepository, times(1)).findByUserId(userId);
        assertEquals(mockedList, result);
    }

}