package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.exporter.ShoppingListPDFExporter;
import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.model.DailyMenu;
import com.lavendor.mealmate.model.ShoppingList;
import com.lavendor.mealmate.service.DailyMenuService;
import com.lavendor.mealmate.service.ShoppingListService;
import com.lavendor.mealmate.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
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

    @GetMapping("/api/id/{id}")
    public ResponseEntity<Map<BasicIngredient, Double>> getElementsFromShoppingList(@PathVariable("id") Long shoppingListId) {
        Map<BasicIngredient, Double> shoppingListElements = shoppingListService.getShoppingListMap(shoppingListId);
        if (shoppingListElements != null) {
            return ResponseEntity.ok(shoppingListElements);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyMap());
        }
    }

    @GetMapping("/api/all")
    public ResponseEntity<List<ShoppingList>> getAllShoppingLists() {
        List<ShoppingList> shoppingLists = shoppingListService.getAllShoppingLists();

        if (shoppingLists != null && !shoppingLists.isEmpty()) {
            return ResponseEntity.ok(shoppingLists);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }
}
