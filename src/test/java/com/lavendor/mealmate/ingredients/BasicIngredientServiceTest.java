package com.lavendor.mealmate.ingredients;

import com.lavendor.mealmate.MealmateApplication;
import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.repository.BasicIngredientRepository;
import com.lavendor.mealmate.service.BasicIngredientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MealmateApplication.class)
public class BasicIngredientServiceTest {

    String testIngredientName = "Minced Meat";
    String testIngredientUnit = "grams";

    @MockBean
    private BasicIngredientRepository basicIngredientRepository;

    @Autowired
    private BasicIngredientService basicIngredientService;

    @BeforeEach
    public void init(){
        testIngredientName = "Minced Meat";
        testIngredientUnit = "grams";
    }

    @Test
    public void testAddBasicIngredient() {

        BasicIngredient expectedIngredient = new BasicIngredient(testIngredientName, testIngredientUnit);

        when(basicIngredientRepository.save(any(BasicIngredient.class))).thenReturn(expectedIngredient);

        BasicIngredient result = basicIngredientService.addBasicIngredient(testIngredientName, testIngredientUnit);

        assertEquals(expectedIngredient, result);
        verify(basicIngredientRepository).save(any(BasicIngredient.class));
    }

    @Test
    public void testGetBasicIngredientById(){
        Long ingredientId = 1L;
        BasicIngredient expectedIngredient = new BasicIngredient(testIngredientName, testIngredientUnit);

        when(basicIngredientRepository.findById(ingredientId)).thenReturn(Optional.of(expectedIngredient));

        BasicIngredient result = basicIngredientService.getBasicIngredientById(ingredientId);

        assertNotNull(result);
        assertEquals(expectedIngredient, result);
    }

    @Test
    void testGetBasicIngredientByIdNotFound() {
        Long ingredientId = 1L;

        when(basicIngredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> basicIngredientService.getBasicIngredientById(ingredientId));
    }
}
