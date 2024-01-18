package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.service.BasicIngredientService;
import com.lavendor.mealmate.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/ingredients")
public class BasicIngredientController {

    private final BasicIngredientService basicIngredientService;
    private final UserService userService;

    public BasicIngredientController(BasicIngredientService basicIngredientService, UserService userService) {
        this.basicIngredientService = basicIngredientService;
        this.userService = userService;
    }

    //Thymeleaf
    @GetMapping()
    public String getBasicIngredientsPage(Authentication authentication, Model model) {
        Long activeUserId = userService.getActiveUserId(authentication);

        List<BasicIngredient> basicIngredientList = basicIngredientService.getBasicIngredientsByUserId(activeUserId);
        basicIngredientList.sort(Comparator.comparing(BasicIngredient::getBasicIngredientName, String.CASE_INSENSITIVE_ORDER));


        model.addAttribute("basicIngredientsList", basicIngredientList);
        model.addAttribute("currentPage", "ingredients");
        return "ingredients";
    }

    @PostMapping("/new-ingredient")
    public String addNewIngredient(@ModelAttribute BasicIngredient basicIngredient,
                                   Authentication authentication) {
        Long activeUserId = userService.getActiveUserId(authentication);
        if (!basicIngredientService.checkIfIngredientExists(basicIngredient.getBasicIngredientName())) {
            basicIngredientService.addBasicIngredient(basicIngredient.getBasicIngredientName(), basicIngredient.getUnit(), activeUserId);
        }

        return "redirect:/ingredients";
    }


    // REST
    @GetMapping("/all")
    public ResponseEntity<List<BasicIngredient>> getAllBasicIngredients() {
        List<BasicIngredient> basicIngredients = basicIngredientService.getAllBasicIngredients();

        if (basicIngredients != null && !basicIngredients.isEmpty()) {
            return ResponseEntity.ok(basicIngredients);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BasicIngredient> getBasicIngredientById(@PathVariable("id") String stringId) {

        Long basicIngredientId = Long.valueOf(stringId);
        BasicIngredient basicIngredient = basicIngredientService.getBasicIngredientById(basicIngredientId);
        return ResponseEntity.ok(basicIngredient);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BasicIngredient> getBasicIngredientByName(@PathVariable("name") String ingredientName) {

        BasicIngredient basicIngredient = basicIngredientService.getBasicIngredientByName(ingredientName.toLowerCase());
        return ResponseEntity.ok(basicIngredient);
    }

//    @DeleteMapping("/id/{id}")
//    public ResponseEntity<String> deleteBasicIngredientById(@PathVariable("id") String stringId) {
//
//        Long basicIngredientId = Long.valueOf(stringId);
//        basicIngredientService.deleteBasicIngredient(basicIngredientId);
//        return ResponseEntity.ok("BasicIngredient with ID: " + basicIngredientId + " deleted successfully");
//    }

}

