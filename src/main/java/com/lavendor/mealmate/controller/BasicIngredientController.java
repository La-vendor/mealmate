package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.service.BasicIngredientService;
import com.lavendor.mealmate.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ingredients")
public class BasicIngredientController {

    private final BasicIngredientService basicIngredientService;
    private final UserService userService;

    public BasicIngredientController(BasicIngredientService basicIngredientService, UserService userService) {
        this.basicIngredientService = basicIngredientService;
        this.userService = userService;
    }

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
        Optional<BasicIngredient> findIngredient = basicIngredientService.getBasicIngredientByNameAndUserId(
                basicIngredient.getBasicIngredientName(),
                activeUserId);
        if (findIngredient.isEmpty()) {
            basicIngredientService.addBasicIngredient(basicIngredient.getBasicIngredientName(), basicIngredient.getUnit(), activeUserId);
        }else{
            throw new DataIntegrityViolationException("Ingredient already exists");
        }

        return "redirect:/ingredients";
    }


}

