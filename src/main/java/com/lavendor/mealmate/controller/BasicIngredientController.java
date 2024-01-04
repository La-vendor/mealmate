package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lavendor.mealmate.service.BasicIngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class BasicIngredientController {

    private final BasicIngredientService basicIngredientService;

    public BasicIngredientController(BasicIngredientService basicIngredientService) {
        this.basicIngredientService = basicIngredientService;
    }


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

