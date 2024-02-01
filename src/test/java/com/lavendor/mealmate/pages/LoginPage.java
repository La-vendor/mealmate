package com.lavendor.mealmate.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "username")
    private WebElement inputUsernameField;

    @FindBy(id = "password")
    private WebElement inputPasswordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    public LoginPage(WebDriver webDriver){
        PageFactory.initElements(webDriver,this);
    }

    public void enterUsername(String username){
        inputUsernameField.sendKeys(username);
    }
    public void enterPassword(String password){
        inputPasswordField.sendKeys(password);
    }
    public void clickLoginButton(){
        loginButton.click();
    }


}
