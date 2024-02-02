package com.lavendor.mealmate.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class DailyMenuPage {


    @FindBy(id = "add_daily_menu")
    private WebElement addDailyMenuButton;

    @FindBy(id = "add_recipe_button")
    private WebElement addRecipeButton;

    @FindBy(id = "generate_button")
    private WebElement generateShoppingListButton;

    @FindBy(css = ".card")
    private List<WebElement> dailyMenuCards;

    public DailyMenuPage(WebDriver webDriver){
        PageFactory.initElements(webDriver,this);
    }

    public void addDailyMenu(){
        addDailyMenuButton.click();
    }

    public void addRecipe(){
        addRecipeButton.click();
    }

    public void generateShoppingList(){
        generateShoppingListButton.click();
    }

    public int getDailyMenuCount() {
        return dailyMenuCards.size();
    }
}
