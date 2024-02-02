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

public class LoginPage {

    @FindBy(id = "username")
    private WebElement inputUsernameField;

    @FindBy(id = "password")
    private WebElement inputPasswordField;

    @FindBy(id = "login_button")
    private WebElement loginButton;

    @FindBy(id = "error_msg")
    private WebElement loginError;

    @FindBy(id = "logout_msg")
    private WebElement logoutMessage;

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
    public String getErrorMessage(){
        return loginError.getText();
    }
    public String getLogoutMessage(){
        return logoutMessage.getText();
    }


    public void login(String username, String password, WebDriver driver, int port) {

        driver.get("http://localhost:" + port + "/login");

        WebDriverWait wait = new WebDriverWait(driver, Duration.of(2, SECONDS));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        this.enterUsername(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        this.enterPassword(password);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_button")));
        this.clickLoginButton();
    }
}
