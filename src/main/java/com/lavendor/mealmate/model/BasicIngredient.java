package com.lavendor.mealmate.model;

import jakarta.persistence.*;

@Entity
@Table(name = "basic_ingredients")
public class BasicIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basicIngredientId;

    private String basicIngredientName;
    private String unit;

    public BasicIngredient() {
    }

    public BasicIngredient(String basicIngredientName) {
        this.basicIngredientName = basicIngredientName;
    }

    public BasicIngredient(String basicIngredientName, String unit) {
        this.basicIngredientName = basicIngredientName;
        this.unit = unit;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
