package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.DailyMenu;
import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.repository.DailyMenuRepository;
import com.lavendor.mealmate.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyMenuService {

    private final DailyMenuRepository dailyMenuRepository;
    private final RecipeRepository recipeRepository;

    public DailyMenuService(DailyMenuRepository dailyMenuRepository, RecipeRepository recipeRepository) {
        this.dailyMenuRepository = dailyMenuRepository;
        this.recipeRepository = recipeRepository;
    }

    public DailyMenu createDailyMenu(LocalDate date) {
        DailyMenu dailyMenu = new DailyMenu(date);
        return dailyMenuRepository.save(dailyMenu);
    }

    public boolean addRecipeToDailyMenu(Long dailyMenuId, Long recipeId) {
        DailyMenu dailyMenu = dailyMenuRepository.findById(dailyMenuId).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
        Recipe recipeToAdd = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        if (dailyMenu != null && recipeToAdd != null) {
            if (!dailyMenu.containsRecipe(recipeToAdd)) {
                dailyMenu.getRecipeList().add(recipeToAdd);
                dailyMenuRepository.save(dailyMenu);
                return true;
            } else {
                return false;
                //Recipe already exists
            }
        }
        return false;
    }

    public DailyMenu getDailyMenuById(Long dailyMenuId) {
        return dailyMenuRepository.findById(dailyMenuId).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
    }

    public DailyMenu getDailyMenuByDate(LocalDate date) {
        return dailyMenuRepository.findByDate(date).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
    }

    public List<DailyMenu> getAllDailyMenu() {
        return dailyMenuRepository.findAll();
    }

    public void deleteDailyMenu(Long dailyMenuId) {
        DailyMenu dailyMenu = dailyMenuRepository.findById(dailyMenuId).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
        dailyMenuRepository.delete(dailyMenu);
    }
}
