package com.lavendor.mealmate.dailymenu;

import com.lavendor.mealmate.exporter.DailyMenuPDFExporter;
import com.lavendor.mealmate.dailymenu.DailyMenu;
import com.lavendor.mealmate.recipe.Recipe;
import com.lavendor.mealmate.dailymenu.DailyMenuService;
import com.lavendor.mealmate.recipe.RecipeService;
import com.lavendor.mealmate.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
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

        dailyMenuService.addRecipeToDailyMenu(dailyMenuId, selectedRecipeId);
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

    @GetMapping("/reset")
    public String resetDailyMenus(Authentication authentication){
        Long activeUserId = userService.getActiveUserId(authentication);
        dailyMenuService.deleteAllByUserId(activeUserId);

        return "redirect:/daily-menu";
    }
}
