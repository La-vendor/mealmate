package com.lavendor.mealmate.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;


    @ManyToMany
    @JoinTable(
            name = "user_dailymenus",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "daily_menu_id")
    )
    private List<DailyMenu> dailyMenus;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }
}
