package com.lavendor.mealmate.service;

import com.lavendor.mealmate.model.DailyMenu;
import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.repository.DailyMenuRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyMenuService {

    private final DailyMenuRepository dailyMenuRepository;

    public DailyMenuService(DailyMenuRepository dailyMenuRepository) {
        this.dailyMenuRepository = dailyMenuRepository;
    }

    public DailyMenu createDailyMenu(LocalDate date){
        DailyMenu dailyMenu = new DailyMenu(date);
        return dailyMenuRepository.save(dailyMenu);
    }

    public DailyMenu addRecipeToDailyMenu(Long dailyMenuId, Recipe recipeToAdd){
        DailyMenu dailyMenu = dailyMenuRepository.findById(dailyMenuId).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
        List<Recipe> recipes = dailyMenu.getRecipeList();
        recipes.add(recipeToAdd);
        dailyMenu.setRecipeList(recipes);

        return dailyMenuRepository.save(dailyMenu);
    }

    public DailyMenu getDailyMenuById(Long dailyMenuId){
        return dailyMenuRepository.findById(dailyMenuId).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
    }

    public DailyMenu getDailyMenuByDate(LocalDate date){
        return dailyMenuRepository.findByDate(date).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
    }

    public List<DailyMenu> getAllDailyMenu(){
        return dailyMenuRepository.findAll();
    }

    public void deleteDailyMenu(Long dailyMenuId){
        DailyMenu dailyMenu = dailyMenuRepository.findById(dailyMenuId).orElseThrow(() -> new EntityNotFoundException("DailyMenu not found"));
        dailyMenuRepository.delete(dailyMenu);
    }
}
