package com.lavendor.mealmate.ingredient;

import com.lavendor.mealmate.exception.IngredientDeleteException;
import com.lavendor.mealmate.user.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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

    @PostMapping("/delete/{id}")
    public RedirectView deleteIngredient(@PathVariable("id") Long ingredientId, RedirectAttributes attributes) {
        String ingredientError = null;

        try {
            basicIngredientService.deleteBasicIngredient(ingredientId);
        }catch (IngredientDeleteException e){
            ingredientError = e.getMessage();
        }
        attributes.addFlashAttribute("ingredientError", ingredientError);
        return new RedirectView("/ingredients", true);
//        return "redirect:/ingredients";
    }

    @GetMapping("/search/{name}")
    public List<IngredientAPI> searchIngredientsByName(@PathVariable("name") String ingredientName){
        return basicIngredientService.searchIngredientsByName(ingredientName);
    }

}

