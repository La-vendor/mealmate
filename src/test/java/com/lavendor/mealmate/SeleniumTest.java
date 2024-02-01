package com.lavendor.mealmate;

import com.lavendor.mealmate.pages.SignupPage;
import com.lavendor.mealmate.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    SignupPage signupPage;

    @Autowired
    private UserService userService;


    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void unauthorizedUserCantAccessHomePage() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void unauthorizedUserCantAccessRandomPage() {
        driver.get("http://localhost:" + this.port + "/random-page");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testSignUpUser(){
        String username = "testUsername";
        String password = "testPassword";

        signupPage = new SignupPage(driver);

        signUp(username, password, password);
        Assertions.assertTrue(userService.getUserByUsername(username).isPresent());
    }

    @Test
    public void testSignUpUserWithMismatchedPassword(){
        String username = "testUsername";
        String password = "testPassword";

        signupPage = new SignupPage(driver);

        String mismatchedPassword = "mismatchedPassword";
        signUp(username, password, mismatchedPassword);

        Assertions.assertFalse(userService.getUserByUsername(username).isPresent());
        Assertions.assertTrue(signupPage.getErrorMessage().contains("Password and confirm password do not match"));
    }


    private void signUp(String username, String password, String confirmPassword) {

        if (!userService.getUserByUsername(username).isPresent()) {
            driver.get("http://localhost:" + port + "/signup");


            WebDriverWait wait = new WebDriverWait(driver, Duration.of(2,SECONDS));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            signupPage.enterUsername(username);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            signupPage.enterPassword(password);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmPassword")));
            signupPage.enterConfirmPassword(confirmPassword);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
            signupPage.clickSignupButton();

        }
    }
}
