package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.exporter.DailyMenuPDFExporter;
import com.lavendor.mealmate.model.DailyMenu;
import com.lavendor.mealmate.model.Recipe;
import com.lavendor.mealmate.service.DailyMenuService;
import com.lavendor.mealmate.service.RecipeService;
import com.lavendor.mealmate.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/daily-menu")
public class DailyMenuController {

    private final DailyMenuService dailyMenuService;
    private final RecipeService recipeService;
    private final UserService userService;

    public DailyMenuController(DailyMenuService dailyMenuService, RecipeService recipeService, UserService userService) {
        this.dailyMenuService = dailyMenuService;
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping()
    public String getDailyMenuPage(Authentication authentication, Model model) {
        Long activeUserId = userService.getActiveUserId(authentication);
        List<DailyMenu> dailyMenus = dailyMenuService.getDailyMenuByUserId(activeUserId);
        List<Recipe> recipeList = recipeService.getRecipesByUserId(activeUserId);
        model.addAttribute("recipeList", recipeList);
        model.addAttribute("dailyMenus", dailyMenus);
        model.addAttribute("currentPage", "daily-menu");
        return "daily-menu";
    }

    @PostMapping("/add")
    public String addDailyMenu(Authentication authentication) {
        Long activeUserId = userService.getActiveUserId(authentication);
        DailyMenu dailyMenu = dailyMenuService.createDailyMenu(activeUserId);

        return "redirect:/daily-menu";
    }

    @PostMapping("/{dailyMenuId}/add-recipe")
    public String addRecipeToDailyMenu(@PathVariable("dailyMenuId") Long dailyMenuId,
                                       @RequestParam("selectedRecipeId") Long selectedRecipeId) {

        boolean recipeAdded = dailyMenuService.addRecipeToDailyMenu(dailyMenuId, selectedRecipeId);
        return "redirect:/daily-menu";
    }

    @PostMapping("/{dailyMenuId}/delete-recipe/{recipeId}")
    public String deleteRecipeFromDailyMenu(@PathVariable("dailyMenuId") Long dailyMenuId,
                                            @PathVariable("recipeId") Long selectedRecipeId) {
        dailyMenuService.deleteRecipeFromDailyMenu(dailyMenuId, selectedRecipeId);
        return "redirect:/daily-menu";
    }

    @PostMapping("/delete/{id}")
    public String deleteDailyMenu(@PathVariable("id") Long id) {
        dailyMenuService.deleteDailyMenu(id);
        return "redirect:/daily-menu";
    }

    @GetMapping(value = "/export-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportShoppingListToPdf(HttpServletResponse response, Authentication authentication) throws IOException {
        Long activeUserId = userService.getActiveUserId(authentication);
        response.setContentType("application/pdf");

        LocalDate localDate = LocalDate.now();
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=daily_menu_" + localDate + ".pdf";

        response.setHeader(headerKey,headerValue);

        List<DailyMenu> dailyMenus = dailyMenuService.getDailyMenuByUserId(activeUserId);
        DailyMenuPDFExporter dailyMenuPDFExporter = new DailyMenuPDFExporter(dailyMenus);
        dailyMenuPDFExporter.export(response);

    }

    @GetMapping("/api/all")
    public ResponseEntity<List<DailyMenu>> getAllDailyMenus() {
        List<DailyMenu> dailyMenus = dailyMenuService.getAllDailyMenu();

        if (dailyMenus != null && !dailyMenus.isEmpty()) {
            return ResponseEntity.ok(dailyMenus);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/api/id/{id}")
    public ResponseEntity<DailyMenu> getDailyMenuById(@PathVariable("id") String stringId) {

        Long dailyMenuId = Long.valueOf(stringId);
        DailyMenu dailyMenu = dailyMenuService.getDailyMenuById(dailyMenuId);
        return ResponseEntity.ok(dailyMenu);
    }

    @DeleteMapping("/api/id/{id}")
    public ResponseEntity<String> deleteDailyMenu(@PathVariable("id") String stringId) {
        Long dailyMenuId = Long.valueOf(stringId);
        dailyMenuService.deleteDailyMenu(dailyMenuId);
        return ResponseEntity.ok("Daily Menu with ID: " + dailyMenuId + " deleted successfully");
    }


//    @PostMapping("/add")
//    public ResponseEntity<String> addDailyMenu() {
//
//        DailyMenu dailyMenu = dailyMenuService.createDailyMenu(LocalDate.now());
//
//        if (dailyMenu != null) {
//            return ResponseEntity.status(HttpStatus.CREATED).body("DailyMenu added successfully!");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add DailyMenu");
//        }
//    }

//    @PostMapping("/{dailyMenuId}/add-recipe/{recipeId}")
//    public ResponseEntity<String> addRecipeToDailyMenu(
//            @PathVariable("dailyMenuId") Long dailyMenuId,
//            @PathVariable("recipeId") Long recipeId) {
//
//        boolean recipeAdded = dailyMenuService.addRecipeToDailyMenu(dailyMenuId,recipeId);
//
//        if(recipeAdded){
//            return ResponseEntity.ok("Recipe: " + recipeId + " added successfully to DailyMenu: " + dailyMenuId);
//        }else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to add recipe to DailyMenu");
//        }
//    }

}
