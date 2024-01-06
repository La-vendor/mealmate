package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.model.DailyMenu;
import com.lavendor.mealmate.service.DailyMenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/daily-menu")
public class DailyMenuController {

    private final DailyMenuService dailyMenuService;

    public DailyMenuController(DailyMenuService dailyMenuService) {
        this.dailyMenuService = dailyMenuService;
    }



    @GetMapping("/all")
    public ResponseEntity<List<DailyMenu>> getAllDailyMenus() {
        List<DailyMenu> dailyMenus = dailyMenuService.getAllDailyMenu();

        if (dailyMenus != null && !dailyMenus.isEmpty()) {
            return ResponseEntity.ok(dailyMenus);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DailyMenu> getDailyMenuById(@PathVariable("id") String stringId) {

        Long dailyMenuId = Long.valueOf(stringId);
        DailyMenu dailyMenu = dailyMenuService.getDailyMenuById(dailyMenuId);
        return ResponseEntity.ok(dailyMenu);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteDailyMenu(@PathVariable("id") String stringId) {
        Long dailyMenuId = Long.valueOf(stringId);
        dailyMenuService.deleteDailyMenu(dailyMenuId);
        return ResponseEntity.ok("Daily Menu with ID: " + dailyMenuId + " deleted successfully");
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDailyMenu() {

        DailyMenu dailyMenu = dailyMenuService.createDailyMenu(LocalDate.now());

        if (dailyMenu != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("DailyMenu added successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add DailyMenu");
        }
    }

    @PostMapping("/{dailyMenuId}/add-recipe/{recipeId}")
    public ResponseEntity<String> addRecipeToDailyMenu(
            @PathVariable("dailyMenuId") Long dailyMenuId,
            @PathVariable("recipeId") Long recipeId) {

        boolean recipeAdded = dailyMenuService.addRecipeToDailyMenu(dailyMenuId,recipeId);

        if(recipeAdded){
            return ResponseEntity.ok("Recipe: " + recipeId + " added successfully to DailyMenu: " + dailyMenuId);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to add recipe to DailyMenu");
        }
    }

}
