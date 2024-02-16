package com.lavendor.mealmate;

import com.lavendor.mealmate.pages.DailyMenuPage;
import com.lavendor.mealmate.pages.LoginPage;
import com.lavendor.mealmate.pages.SignupPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DailyMenuTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    SignupPage signupPage;
    LoginPage loginPage;
    DailyMenuPage dailyMenuPage;

    String username = "testUsername";
    String password = "testPassword";


    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        signupPage = new SignupPage(driver);
        loginPage = new LoginPage(driver);
        signupPage.signUp(username, password, password,driver,port);
        loginPage.login(username, password,driver,port);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void addAndResetDailyMenu(){
        driver.get("http://localhost:" + port + "/daily-menu");
        dailyMenuPage = new DailyMenuPage(driver);

        dailyMenuPage.addDailyMenu();
        assertEquals(1, dailyMenuPage.getDailyMenuCount());
        dailyMenuPage.resetDailyMenus();
        assertEquals(0, dailyMenuPage.getDailyMenuCount());
    }

    @Test
    public void addTwoDailyMenus(){
        driver.get("http://localhost:" + port + "/daily-menu");
        dailyMenuPage = new DailyMenuPage(driver);

        dailyMenuPage.addDailyMenu();
        dailyMenuPage.addDailyMenu();
        assertEquals(2, dailyMenuPage.getDailyMenuCount());
        dailyMenuPage.resetDailyMenus();
    }
}
