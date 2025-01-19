import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SauceDemoTest {

    public static void main(String[] args) {
        WebDriver driver = setupDriver("chrome");

        try {
            testInvalidLogin(driver);
            testValidLoginAndCheckout(driver);
        } finally {
            driver.quit();
        }
    }

    public static WebDriver setupDriver(String browser) {
        WebDriver driver;
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:/path/to/chromedriver.exe");
            driver = new ChromeDriver();
        } else {
            System.setProperty("webdriver.gecko.driver", "C:/path/to/geckodriver.exe");
            driver = new FirefoxDriver();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }

    public static void testInvalidLogin(WebDriver driver) {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("invalid_user");
        driver.findElement(By.id("password")).sendKeys("invalid_password");
        driver.findElement(By.id("login-button")).click();
        WebElement errorMessage = driver.findElement(By.cssSelector(".error-message-container"));
        System.out.println(errorMessage.isDisplayed() ? "Test Invalid Login: Passed" : "Test Invalid Login: Failed");
    }

    public static void testValidLoginAndCheckout(WebDriver driver) {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("inventory.html"));

        driver.findElement(By.cssSelector(".inventory_item:nth-child(1) .btn_inventory")).click();
        driver.findElement(By.cssSelector(".inventory_item:nth-child(2) .btn_inventory")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("cart.html"));
        driver.findElement(By.id("checkout")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("checkout-step-one.html"));
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("checkout-step-two.html"));
        driver.findElement(By.id("finish")).click();
        WebElement thankYouMessage = driver.findElement(By.cssSelector(".complete-header"));

        System.out.println(thankYouMessage.getText().contains("Thank you") ? "Test Valid Login and Checkout: Passed" : "Test Valid Login and Checkout: Failed");
    }
}
