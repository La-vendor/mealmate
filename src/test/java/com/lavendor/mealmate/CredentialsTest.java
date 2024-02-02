package com.lavendor.mealmate;

import com.lavendor.mealmate.pages.LoginPage;
import com.lavendor.mealmate.pages.SignupPage;
import com.lavendor.mealmate.service.UserService;
import org.junit.jupiter.api.AfterEach;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialsTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    SignupPage signupPage;
    LoginPage loginPage;

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
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void unauthorizedUserCantAccessRandomPage() {
        driver.get("http://localhost:" + this.port + "/random-page");
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testSignUpUser() {
        String username = "testUsername";
        String password = "testPassword";

        signupPage = new SignupPage(driver);

        signupPage.signUp(username, password,password,driver,port);
        assertTrue(userService.getUserByUsername(username).isPresent());
    }

    @Test
    public void testSignUpUserWithMismatchedPassword() {
        String username = "testMismatchedUsername";
        String password = "testMismatchedPassword";
        String mismatchedPassword = "mismatchedPassword";

        signupPage = new SignupPage(driver);

        signupPage.signUp(username, password, mismatchedPassword,driver,port);

        assertFalse(userService.getUserByUsername(username).isPresent());
        assertTrue(signupPage.getErrorMessage().contains("Password and confirm password do not match"));
    }

    @Test
    public void testSignUpUserWithUsernameAlreadyTaken() {
        String username = "testUsername";
        String password = "testPassword";

        signupPage = new SignupPage(driver);
        signupPage.signUp(username, password, password,driver,port);
        assertTrue(userService.getUserByUsername(username).isPresent());

        signupPage.signUp(username, password, password,driver,port);

        assertTrue(signupPage.getErrorMessage().contains("Username is not available"));
    }

    @Test
    public void testSignUpAndLogin() {
        String username = "testUsername";
        String password = "testPassword";

        signupPage = new SignupPage(driver);
        signupPage.signUp(username, password, password,driver,port);

        loginPage = new LoginPage(driver);
        loginPage.login(username, password,driver,port);

        assertEquals("Daily Menu", driver.getTitle());
    }

    @Test
    public void testLoginWithIncorrectUsername(){
        String username = "testUsername";
        String wrongUsername = "wrongUsername";
        String password = "testPassword";

        signupPage = new SignupPage(driver);
        signupPage.signUp(username, password, password,driver,port);
        assertTrue(userService.getUserByUsername(username).isPresent());
        loginPage = new LoginPage(driver);
        loginPage.login(wrongUsername, password,driver,port);

        assertTrue(loginPage.getErrorMessage().contains("Invalid username or password"));
    }

    @Test
    public void testLoginWithIncorrectPassword(){
        String username = "testUsername";
        String password = "testPassword";
        String wrongPassword = "wrongPassword";

        signupPage = new SignupPage(driver);
        signupPage.signUp(username, password, password,driver,port);
        assertTrue(userService.getUserByUsername(username).isPresent());
        loginPage = new LoginPage(driver);
        loginPage.login(username, wrongPassword,driver,port);

        WebDriverWait wait = new WebDriverWait(driver, Duration.of(2, SECONDS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error_msg")));

        assertTrue(loginPage.getErrorMessage().contains("Invalid username or password"));
    }


}
