package com.lavendor.mealmate.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "daily_menus")
public class DailyMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyMenuId;

    private LocalDate date;
    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "daily_menu_recipes",
            joinColumns = @JoinColumn(name = "daily_menu_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<Recipe> recipeList;

    public DailyMenu() {
    }




    public DailyMenu(LocalDate date, Long userId) {
        this.date = date;
        this.userId = userId;
    }

    public Long getDailyMenuId() {
        return dailyMenuId;
    }

    public void setDailyMenuId(Long dailyMenuId) {
        this.dailyMenuId = dailyMenuId;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public boolean containsRecipe(Recipe recipe) {
        return recipeList.contains(recipe);
    }
}
