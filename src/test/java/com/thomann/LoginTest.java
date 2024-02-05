package com.thomann;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest {
    //username: tld0rt+7ge8vc33z4vkk@sharklasers.com
    //password: acEastAestePar0lamEAunicA:)
    WebDriver driver;
    String url = "https://www.thomann.de/ro/index.html";
    int consentPopup = 0;
    @BeforeTest
    @Parameters({"browserChoice"})
    public void setup(String browser){
        switch(browser){
            case "chrome": driver = new ChromeDriver(); break;
            case "firefox": driver = new FirefoxDriver(); break;
            case "edge" : driver = new EdgeDriver(); break;
            default: driver = new ChromeDriver();
        }
        driver.get(url);
        driver.manage().window().maximize();
        WebElement cookiesButton = driver.findElement(By.className("js-decline-all-cookies"));
        cookiesButton.click();
        consentPopup++;
        sleep(2000);
    }
    @Test
    @Parameters({"usernameP", "passwordP", "nameP", "fullNameP"})
    public void login(String username, String password, String name, String fullName) {
        Assert.assertEquals(driver.getCurrentUrl(), url);
        Assert.assertTrue(consentPopup>0);
        System.out.println("Consent popup was closed");
        consentPopup--;
        WebElement loginMenuButton = driver.findElement(By.xpath("//div[@class=\"user-navigation js-user-navigation\"]/div[2]/a"));
        loginMenuButton.click();
        sleep(1000);
        Assert.assertEquals(loginMenuButton.getAttribute("aria-expanded"), "true");
        WebElement usernameInput = driver.findElement(By.id("uname"));
        usernameInput.sendKeys(username);
        WebElement passwordInput = driver.findElement(By.id("passw"));
        passwordInput.sendKeys(password);
        WebElement loginButton = driver.findElement(By.className("login__submit"));
        loginButton.click();
        WebElement loadingImage = driver.findElement(By.xpath("//form[@id=\"flyin-login-form\"]/div[1]/div[7]/img"));
        Assert.assertTrue(loadingImage.isDisplayed());
        sleep(2000);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement cookiesButton = driver.findElement(By.className("js-decline-all-cookies"));
            wait.until(ExpectedConditions.elementToBeClickable(cookiesButton));
            if (cookiesButton.isDisplayed())
                consentPopup++;
            cookiesButton.click();
            sleep(2000);
        } catch (Exception e) {
            System.out.println("Consent popup was not displayed.");
        }
        finally {
            if (consentPopup > 0) {
                System.out.println("Consent popup was closed.");
                Assert.assertTrue(consentPopup > 0);
            }
//            WebElement homeGreetingMessage = driver.findElement(By.xpath("//div[@class=\"home-greeter\"]/h2"));
//            Assert.assertTrue(homeGreetingMessage.getText().contains(name));
            WebElement userMenuButton = driver.findElement(By.xpath("//div[@class=\"user-navigation js-user-navigation\"]/div[2]/a"));
            userMenuButton.click();
            sleep(1000);
            Assert.assertEquals(userMenuButton.getAttribute("aria-expanded"), "true");
            WebElement userAccountInfo = driver.findElement(By.className("mythomann-flyin-customer-info"));
            Assert.assertTrue(userAccountInfo.getText().contains(fullName));
            Assert.assertTrue(userAccountInfo.getText().contains(username));
        }
    }
    @AfterTest(alwaysRun = true)
    public void tearDown(){
        driver.close();
    }
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
