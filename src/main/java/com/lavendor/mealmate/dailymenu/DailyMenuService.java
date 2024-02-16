package com.lavendor.mealmate.dailymenu;

import com.lavendor.mealmate.recipe.Recipe;
import com.lavendor.mealmate.recipe.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class DailyMenuService {

    private final DailyMenuRepository dailyMenuRepository;
    private final RecipeRepository recipeRepository;

    public DailyMenuService(DailyMenuRepository dailyMenuRepository, RecipeRepository recipeRepository) {
        this.dailyMenuRepository = dailyMenuRepository;
        this.recipeRepository = recipeRepository;
    }

    public DailyMenu createDailyMenu(Long activeUserId) {

        if (activeUserId != null) {
            DailyMenu dailyMenu = new DailyMenu(getNewDate(), activeUserId);
            return dailyMenuRepository.save(dailyMenu);
        } else {
            return null;
        }
    }

    public LocalDate getNewDate() {
        List<DailyMenu> dailyMenus = dailyMenuRepository.findAll();

        LocalDate lastDate;
        if (!dailyMenus.isEmpty()) {
            lastDate = dailyMenus.stream()
                    .map(DailyMenu::getDate)
                    .max(LocalDate::compareTo)
                    .orElse(LocalDate.now());
            return lastDate.plusDays(1);
        } else {
            return LocalDate.now();
        }
    }

    public DailyMenu addRecipeToDailyMenu(Long dailyMenuId, Long recipeId) {
        DailyMenu dailyMenu = dailyMenuRepository.findById(dailyMenuId).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
        Recipe recipeToAdd = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        if (dailyMenu != null && recipeToAdd != null) {
            if (!dailyMenu.containsRecipe(recipeToAdd)) {
                dailyMenu.getRecipeList().add(recipeToAdd);
                return dailyMenuRepository.save(dailyMenu);
            } else {
                return null;
                //Recipe already exists
            }
        }
        return null;
    }

    public DailyMenu deleteRecipeFromDailyMenu(Long dailyMenuId, Long recipeId) {
        DailyMenu dailyMenu = dailyMenuRepository.findById(dailyMenuId).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
        List<Recipe> recipeList = dailyMenu.getRecipeList();
        recipeList.removeIf(recipe -> Objects.equals(recipe.getRecipeId(), recipeId));
        dailyMenu.setRecipeList(recipeList);
        return dailyMenuRepository.save(dailyMenu);
    }

    public DailyMenu getDailyMenuById(Long dailyMenuId) {
        return dailyMenuRepository.findById(dailyMenuId).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
    }

    public List<DailyMenu> getDailyMenuByUserId(Long userId) {
        return dailyMenuRepository.findByUserId(userId);
    }

    public void deleteDailyMenu(Long dailyMenuId) {
        DailyMenu dailyMenu = dailyMenuRepository.findById(dailyMenuId).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
        dailyMenuRepository.delete(dailyMenu);
    }

    @Transactional
    public void deleteAllByUserId(Long activeUserId) {
        dailyMenuRepository.deleteByUserId(activeUserId);
    }
}
