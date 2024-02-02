package com.lavendor.mealmate.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class SignupPage {

    @FindBy(id = "username")
    private WebElement inputUsernameField;

    @FindBy(id = "password")
    private WebElement inputPasswordField;

    @FindBy(id = "confirm_password")
    private WebElement inputConfirmPasswordField;

    @FindBy(id = "signup_button")
    private WebElement signupButton;

    @FindBy(id = "signup_error")
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

    public void signUp(String username, String password, String confirmPassword, WebDriver driver, int port) {

        driver.get("http://localhost:" + port + "/signup");

        WebDriverWait wait = new WebDriverWait(driver, Duration.of(2, SECONDS));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        this.enterUsername(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        this.enterPassword(password);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirm_password")));
        this.enterConfirmPassword(confirmPassword);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signup_button")));
        this.clickSignupButton();
    }
}
