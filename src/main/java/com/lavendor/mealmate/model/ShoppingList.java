package com.lavendor.mealmate.model;

import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "shopping_lists")
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingListId;
    private String name;
    private Long userId;

    @ElementCollection
    @CollectionTable(
            name = "shopping_list_ingredients",
            joinColumns = @JoinColumn(name = "shopping_list_id")
    )
    @MapKeyJoinColumn(name = "ingredient_id")
    @Column(name = "quantity")
    private Map<BasicIngredient, Double> ingredientQuantityMap;

    public ShoppingList() {
    }

    public ShoppingList(String name, Map<BasicIngredient, Double> ingredientQuantityMap){
        this.name = name;
        this.ingredientQuantityMap = ingredientQuantityMap;
    }

    public void addShoppingListElement(Map<BasicIngredient, Double> additionalShoppingListElements) {
        this.ingredientQuantityMap.putAll(additionalShoppingListElements);
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<BasicIngredient, Double> getIngredientQuantityMap() {
        return ingredientQuantityMap;
    }

    public void setIngredientQuantityMap(Map<BasicIngredient, Double> ingredientQuantityMap) {
        this.ingredientQuantityMap = ingredientQuantityMap;
    }
}
