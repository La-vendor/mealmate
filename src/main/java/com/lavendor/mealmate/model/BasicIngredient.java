package com.lavendor.mealmate.model;

import jakarta.persistence.*;

@Entity
@Table(name = "basic_ingredients")
public class BasicIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basicIngredientId;

    private String basicIngredientName;

    public BasicIngredient() {
    }

    public BasicIngredient(String basicIngredientName) {
        this.basicIngredientName = basicIngredientName;
    }

    public Long getBasicIngredientId() {
        return basicIngredientId;
    }

    public void setBasicIngredientId(Long basicIngredientId) {
        this.basicIngredientId = basicIngredientId;
    }

    public String getBasicIngredientName() {
        return basicIngredientName;
    }

    public void setBasicIngredientName(String basicIngredientName) {
        this.basicIngredientName = basicIngredientName;
    }
}
