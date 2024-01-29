package com.thomann;

import org.openqa.selenium.*;
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

public class SchimbaLimbaSauUnitateaMonetaraTest {
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
    @Parameters({"languageSelectedP", "currencySelectedP"})
    public void chooseCountry(String languageSelected, String currencySelected) {
        WebElement countryMenuButton = driver.findElement(By.className("shop-country"));
        countryMenuButton.click(); sleep(2000);
        WebElement chooseCountryButton = driver.findElement(By.xpath("//div[@class='country-selection']//div[2]"));
        chooseCountryButton.click(); //sleep(2000);

        WebElement chooseLanguageButton = driver.findElement(By.xpath("//*[@id=\"js-shop-selection\"]/div/div[2]/div/div[5]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", chooseLanguageButton);
        chooseLanguageButton.click(); //sleep(2000);

        Select dropdown = new Select(driver.findElement(By.xpath("//*[@id=\"js-shop-selection\"]/div/div[3]/div/select")));
        dropdown.selectByValue("74");
        WebElement option = driver.findElement(By.xpath("//option[31]"));
        Assert.assertTrue(option.isSelected());
        //sleep(2000);

        WebElement saveButton = driver.findElement(By.xpath("//*[@id=\"js-shop-selection\"]/div/div[4]/button"));
        saveButton.click();
        sleep(4000);

//        WebElement cookiePopup = driver.findElement(By.className("js-decline-all-cookies"));
//        if(cookiePopup.is) {
//            cookiePopup.click();
//            sleep(2000);
//        }
//        else {
//            System.out.println("Consent popup is not displayed");
//        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
            WebElement cookiesButton = driver.findElement(By.className("js-decline-all-cookies"));
            wait.until(ExpectedConditions.elementToBeClickable(cookiesButton));
            cookiesButton.click();
            sleep(4000);
        } catch (Exception e) {
            WebElement languageAndCurrencyDisplayed = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div[1]/div[1]/div[3]/div/div[1]/span"));
            Assert.assertTrue(languageAndCurrencyDisplayed.getText().contains(languageSelected));
            Assert.assertTrue(languageAndCurrencyDisplayed.getText().contains(currencySelected));
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
