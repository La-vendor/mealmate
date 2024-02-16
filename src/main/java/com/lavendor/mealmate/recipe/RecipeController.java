package com.lavendor.mealmate.recipe;

import com.lavendor.mealmate.ingredient.BasicIngredient;
import com.lavendor.mealmate.recipe.Recipe;
import com.lavendor.mealmate.recipeingredient.RecipeIngredient;
import com.lavendor.mealmate.recipeingredient.RecipeIngredientRepository;
import com.lavendor.mealmate.ingredient.BasicIngredientService;
import com.lavendor.mealmate.recipeingredient.RecipeIngredientService;
import com.lavendor.mealmate.recipe.RecipeService;
import com.lavendor.mealmate.user.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//TODO Make so that every element on recipe list is a button that takes you to list of ingredients of that recipe
@Controller
@RequestMapping("/recipe")
@SessionAttributes({"temporaryIngredients", "recipeIngredients"})
public class RecipeController {

    private final RecipeService recipeService;
    private final BasicIngredientService basicIngredientService;
    private final UserService userService;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeIngredientService recipeIngredientService;

    @ModelAttribute("temporaryIngredients")
    public List<RecipeIngredient> temporaryIngredients() {
        return new ArrayList<>();
    }

    @ModelAttribute("recipeIngredients")
    public List<RecipeIngredient> recipeIngredients() {
        return new ArrayList<>();
    }

    public RecipeController(RecipeService recipeService, BasicIngredientService basicIngredientService, UserService userService,
                            RecipeIngredientRepository recipeIngredientRepository, RecipeIngredientService recipeIngredientService) {
        this.recipeService = recipeService;
        this.basicIngredientService = basicIngredientService;
        this.userService = userService;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeIngredientService = recipeIngredientService;
    }

    @GetMapping()
    public String getRecipePage(@ModelAttribute("temporaryIngredients") List<RecipeIngredient> temporaryIngredients,
                                @ModelAttribute("recipeIngredients") List<RecipeIngredient> recipeIngredients,
                                Authentication authentication,
                                Model model) {
        Long activeUserId = userService.getActiveUserId(authentication);
        List<Recipe> recipeList = recipeService.getRecipesByUserId(activeUserId);
        List<BasicIngredient> basicIngredientList = basicIngredientService.getBasicIngredientsByUserId(activeUserId);
        basicIngredientList.sort(Comparator.comparing(BasicIngredient::getBasicIngredientName, String.CASE_INSENSITIVE_ORDER));

        model.addAttribute("recipeIngredients", recipeIngredients);
        model.addAttribute("temporaryIngredients", temporaryIngredients);
        model.addAttribute("basicIngredientList", basicIngredientList);
        model.addAttribute("recipeList", recipeList);
        model.addAttribute("currentPage", "recipe");
        return "recipe";
    }


    @PostMapping("/add-ingredient")
    public String addIngredientToRecipe(@RequestParam("selectedIngredientId") Long selectedIngredientId,
                                        @RequestParam("newIngredientQuantity") Double newIngredientQuantity,
                                        @SessionAttribute("temporaryIngredients") List<RecipeIngredient> temporaryIngredients) {
        RecipeIngredient selectedIngredient = new RecipeIngredient(basicIngredientService.getBasicIngredientById(selectedIngredientId), newIngredientQuantity);

        temporaryIngredients.add(selectedIngredient);
        return "redirect:/recipe";
    }

    @GetMapping("/reset")
    public String deleteIngredient(@SessionAttribute("temporaryIngredients") List<RecipeIngredient> temporaryIngredients) {
        temporaryIngredients.clear();
        return "redirect:/recipe";
    }

    @PostMapping("/add")
    public String addRecipe(@RequestParam("newRecipeName") String newRecipeName,
                            @SessionAttribute("temporaryIngredients") List<RecipeIngredient> temporaryIngredients,
                            Authentication authentication) {

        Long activeUserId = userService.getActiveUserId(authentication);
        try {
            Recipe newRecipe = recipeService.createRecipe(newRecipeName, temporaryIngredients, activeUserId);
//            recipeIngredientService.addRecipeToIngredients(temporaryIngredients, newRecipe);
            temporaryIngredients.clear();
        }catch(DataIntegrityViolationException e){
            e.printStackTrace();
        }
        return "redirect:/recipe";
    }

    @GetMapping("/ingredients/{recipeId}")
    public String displayRecipeIngredient(@PathVariable("recipeId") Long recipeId,
                                          @SessionAttribute("recipeIngredients") List<RecipeIngredient> recipeIngredients) {
        recipeIngredients.clear();
        recipeIngredients.addAll(recipeService.getRecipeById(recipeId).getIngredients());
        return "redirect:/recipe";
    }

    @PostMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable("id") String stringId) {
        Long recipeId = Long.valueOf(stringId);
        recipeService.deleteRecipe(recipeId);
        return "redirect:/recipe";
    }
}
