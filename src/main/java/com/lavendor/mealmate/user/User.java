package com.lavendor.mealmate.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;


//    @ManyToMany
//    @JoinTable(
//            name = "user_dailymenus",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "daily_menu_id"))
//
//    private List<DailyMenu> dailyMenus;

//    @OneToMany
//    @JoinTable(
//            name = "user_recipes",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
//    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
//    private List<Recipe> recipes;
//
//    @ManyToMany
//    @JoinTable(
//            name = "user_ingredients",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "basic_ingredient_id"))
//    private List<BasicIngredient> ingredients;

    public User() {
    }


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public List<DailyMenu> getDailyMenus() {
//        return dailyMenus;
//    }
//
//    public void setDailyMenus(List<DailyMenu> dailyMenus) {
//        this.dailyMenus = dailyMenus;
//    }

//    public List<Recipe> getRecipes() {
//        return recipes;
//    }
//
//    public void setRecipes(List<Recipe> recipes) {
//        this.recipes = recipes;
//    }
//
//    public List<BasicIngredient> getIngredients() {
//        return ingredients;
//    }
//
//    public void setIngredients(List<BasicIngredient> ingredients) {
//        this.ingredients = ingredients;
//    }
}
