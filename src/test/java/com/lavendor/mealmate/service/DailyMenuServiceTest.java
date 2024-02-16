package com.lavendor.mealmate.service;

import com.lavendor.mealmate.MealmateApplication;
import com.lavendor.mealmate.dailymenu.DailyMenu;
import com.lavendor.mealmate.dailymenu.DailyMenuRepository;
import com.lavendor.mealmate.dailymenu.DailyMenuService;
import com.lavendor.mealmate.recipe.Recipe;
import com.lavendor.mealmate.recipe.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@SpringBootTest(classes = MealmateApplication.class)
public class DailyMenuServiceTest {

    private final Long userId = 100L;
    private final Long recipeId = 100L;
    private final Long dailyMenuId = 100L;
    private DailyMenu testDailyMenu;
    private final LocalDate testDate = LocalDate.now();

    @MockBean
    private DailyMenuRepository dailyMenuRepository;

    @MockBean
    private RecipeRepository recipeRepository;

    @Autowired
    private DailyMenuService dailyMenuService;

    @BeforeEach
    void init(){
        testDailyMenu = new DailyMenu(testDate,userId);
    }

    @Test
    public void testCreateDailyMenu(){

        when(dailyMenuRepository.save(any(DailyMenu.class))).thenReturn(testDailyMenu);

        DailyMenu result = dailyMenuService.createDailyMenu(userId);

        assertNotNull(result);
        assertEquals(testDailyMenu,result);
    }

    @Test
    public void testCreateTwoDailyMenus(){
        DailyMenu expectedFirstDailyMenu = new DailyMenu(testDate,userId);
        DailyMenu expectedSecondDailyMenu = new DailyMenu(testDate.plusDays(1),userId);

        when(dailyMenuRepository.save(any(DailyMenu.class))).thenAnswer(invocation -> invocation.getArgument(0));
        DailyMenu resultDateNow = dailyMenuService.createDailyMenu(userId);

        when(dailyMenuRepository.findAll()).thenReturn(List.of(expectedFirstDailyMenu));
        when(dailyMenuRepository.save(any(DailyMenu.class))).thenAnswer(invocation -> invocation.getArgument(0));
        DailyMenu resultDateNowPlusOneDay = dailyMenuService.createDailyMenu(userId);

        assertNotNull(resultDateNow);
        assertNotNull(resultDateNowPlusOneDay);

        assertEquals(expectedFirstDailyMenu.getDate(),resultDateNow.getDate());
        assertEquals(expectedSecondDailyMenu.getDate(),resultDateNowPlusOneDay.getDate());
    }

    @Test
    public void setAddRecipeToDailyMenu(){

        Recipe testRecipe = new Recipe("Test recipe");

        when(dailyMenuRepository.findById(dailyMenuId)).thenReturn(Optional.of(testDailyMenu));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(testRecipe));
        when(dailyMenuRepository.save(any(DailyMenu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DailyMenu result = dailyMenuService.addRecipeToDailyMenu(dailyMenuId,recipeId);

        verify(dailyMenuRepository,times(1)).save(testDailyMenu);

        assertNotNull(result);
        assertTrue(result.getRecipeList().contains(testRecipe));
    }

    @Test
    public void testAddRecipeToDailyMenu_RecipeAlreadyExists() {

        Recipe testRecipe = new Recipe();

        when(dailyMenuRepository.findById(dailyMenuId)).thenReturn(Optional.of(testDailyMenu));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(testRecipe));

        testDailyMenu.getRecipeList().add(testRecipe);

        DailyMenu result = dailyMenuService.addRecipeToDailyMenu(dailyMenuId,recipeId);

        verify(dailyMenuRepository, never()).save(testDailyMenu);

        assertNull(result);
    }

    @Test
    public void testDeleteRecipeFromDailyMenu() {
        Recipe testRecipe = new Recipe();
        testDailyMenu.getRecipeList().add(testRecipe);

        when(dailyMenuRepository.findById(anyLong())).thenReturn(Optional.of(testDailyMenu));
        when(dailyMenuRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DailyMenu result = dailyMenuService.deleteRecipeFromDailyMenu(dailyMenuId, recipeId);

        verify(dailyMenuRepository, times(1)).save(testDailyMenu);
        assertTrue(result.containsRecipe(testRecipe));
    }

    @Test
    public void testDeleteRecipeFromDailyMenu_DailyMenuNotFound() {
        when(dailyMenuRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dailyMenuService.deleteRecipeFromDailyMenu(dailyMenuId, recipeId));
    }

    @Test
    public void testGetDailyMenuById() {

        when(dailyMenuRepository.findById(anyLong())).thenReturn(Optional.of(testDailyMenu));

        DailyMenu result = dailyMenuService.getDailyMenuById(dailyMenuId);

        assertNotNull(result);
        assertEquals(testDailyMenu,result);
    }

    @Test
    public void testGetDailyMenuById_DailyMenuNotFound() {
        when(dailyMenuRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dailyMenuService.getDailyMenuById(dailyMenuId));
    }

    @Test
    public void testGetDailyMenuByUserId() {

        when(dailyMenuRepository.findByUserId(anyLong())).thenReturn(List.of(testDailyMenu));

        List<DailyMenu> result = dailyMenuService.getDailyMenuByUserId(userId);

        assertNotNull(result);
        assertTrue(result.contains(testDailyMenu));
    }

    @Test
    public void testDeleteDailyMenu() {

        when(dailyMenuRepository.findById(anyLong())).thenReturn(Optional.of(testDailyMenu));

        dailyMenuService.deleteDailyMenu(dailyMenuId);

        verify(dailyMenuRepository, times(1)).delete(testDailyMenu);
    }

    @Test
    public void testDeleteDailyMenu_DailyMenuNotFound() {
        when(dailyMenuRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dailyMenuService.deleteDailyMenu(dailyMenuId));
    }

}
