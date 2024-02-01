package com.lavendor.mealmate.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id = "username")
    private WebElement inputUsernameField;

    @FindBy(id = "password")
    private WebElement inputPasswordField;

    @FindBy(id = "confirmPassword")
    private WebElement inputConfirmPasswordField;

    @FindBy(id = "buttonSignUp")
    private WebElement signupButton;

    @FindBy(id = "signupError")
    private WebElement signupError;

    public SignupPage(WebDriver webDriver){
        PageFactory.initElements(webDriver,this);
    }

    public void enterUsername(String username){
        inputUsernameField.sendKeys(username);
    }
    public void enterPassword(String password){
        inputPasswordField.sendKeys(password);
    }
    public void enterConfirmPassword(String password){
        inputConfirmPasswordField.sendKeys(password);
    }
    public void clickSignupButton(){
        signupButton.click();
    }

    public String getErrorMessage(){
        return signupError.getText();
    }
}
