package com.lavendor.mealmate.shoppinglist;

import com.lavendor.mealmate.exporter.ShoppingListPDFExporter;
import com.lavendor.mealmate.ingredient.BasicIngredient;
import com.lavendor.mealmate.dailymenu.DailyMenu;
import com.lavendor.mealmate.shoppinglist.ShoppingList;
import com.lavendor.mealmate.dailymenu.DailyMenuService;
import com.lavendor.mealmate.shoppinglist.ShoppingListService;
import com.lavendor.mealmate.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final DailyMenuService dailyMenuService;
    private final UserService userService;

    public ShoppingListController(ShoppingListService shoppingListService, DailyMenuService dailyMenuService, UserService userService) {
        this.shoppingListService = shoppingListService;
        this.dailyMenuService = dailyMenuService;
        this.userService = userService;
    }

    @GetMapping()
    public String getRecipePage(Authentication authentication, Model model) {
        Long activeUserId = userService.getActiveUserId(authentication);
        Map<BasicIngredient, Double> ingredientQuantityMap = shoppingListService.getShoppingListByUserId(activeUserId);

        Map<BasicIngredient, Double> sortedIngredientQuantityMap = ingredientQuantityMap.entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getBasicIngredientName(), String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        model.addAttribute("ingredientQuantityMap", sortedIngredientQuantityMap);
        model.addAttribute("currentPage", "shopping-list");
        return "shopping-list";
    }


    @GetMapping("/generate")
    public String generateShoppingList(Authentication authentication) {
        Long activeUserId = userService.getActiveUserId(authentication);
        List<DailyMenu> dailyMenus = dailyMenuService.getDailyMenuByUserId(activeUserId);
        ShoppingList shoppingList = shoppingListService.generateOrUpdateShoppingList(activeUserId, dailyMenus);

        return "redirect:/shopping-list";
    }

    @GetMapping(value = "/export-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportShoppingListToPdf(Authentication authentication, HttpServletResponse response) throws IOException {
        Long activeUserId = userService.getActiveUserId(authentication);

        response.setContentType("application/pdf");
        LocalDate localDate = LocalDate.now();
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=shopping_list_" + localDate + ".pdf";
        response.setHeader(headerKey, headerValue);

        Map<BasicIngredient, Double> shoppingListMap = shoppingListService.getShoppingListByUserId(activeUserId);

        ShoppingListPDFExporter shoppingListPDFExporter = new ShoppingListPDFExporter(shoppingListMap);
        shoppingListPDFExporter.export(response);

    }
}
