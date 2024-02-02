package com.thomann;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class ReviewLimbaOriginalaTest {
    WebDriver driver;
    String url = "https://www.thomann.de/ro/index.html";
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
        sleep(2000);
    }
    @Test
    @Parameters({"revertTranslationP"})
    public void cosProduse(String revertTranslation) {
        int consentPopup = 0;
        WebElement mostSelledBrandProducts = driver.findElement(By.xpath("//li[@data-identifier=\"tab-topseller-tab-2\"]/a"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1400)", "");
        mostSelledBrandProducts.click();
        sleep(2000);
        WebElement firstProductDisplayed = driver.findElement(By.xpath("//div[@id=\"fx-product-carousel-tabbed-topseller-tab-2\"]/div/div[2]/div/div[1]/div/div[1]/div[1]"));
        firstProductDisplayed.click();
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
        finally{
            if (consentPopup>0)
                System.out.println("Consent popup was closed.");
            js.executeScript("window.scrollBy(0,5000)", "");
            WebElement reviewsPageButton = driver.findElement(By.xpath("//div[@class=\"product-reviews-detail-teaser\"]/div[2]/a"));
            reviewsPageButton.click();
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
                if (consentPopup > 0)
                    System.out.println("Consent popup was closed.");
                js.executeScript("window.scrollBy(0,400)", "");
                Select dropdown = new Select(driver.findElement(By.name("reviewlang[]")));
                WebElement dropdownButtonClick = driver.findElement(By.xpath("//form[@class=\"js-reviews-form\"]/div/div[3]"));
                dropdownButtonClick.click();
                sleep(1000);
                WebElement dropdownLanguageSelect = driver.findElement(By.xpath("//form[@class=\"js-reviews-form\"]/div/div[3]/div/div[2]/div[2]/div/div[2]"));
                dropdownLanguageSelect.click();
                sleep(1000);
                WebElement translateReviewToselectedLanguageButton = driver.findElement(By.xpath("//div[@class=\"product-reviews-content__reviews js-reviews\"]/div[1]/div[1]/div/div[1]/a"));
                translateReviewToselectedLanguageButton.click();
                sleep(1000);
                WebElement revertReviewTranslationButton = driver.findElement(By.xpath("//div[@class=\"product-reviews-content__reviews js-reviews\"]/div[1]/div[1]/div/div[2]/a"));
                Assert.assertTrue(revertReviewTranslationButton.getText().contains(revertTranslation));
            }
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
