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
    @Parameters({"languageSelectedP", "currencySelectedP", "countrySelectedP"})
    public void chooseCountry(String languageSelected, String currencySelected, String countrySelected) {
        Assert.assertEquals(driver.getCurrentUrl(), url);
        System.out.println("The application is open.");
        Assert.assertTrue(consentPopup>0);
        System.out.println("Consent popup was closed.");
        consentPopup--;
        WebElement countryMenuButton = driver.findElement(By.className("shop-country"));
        countryMenuButton.click(); sleep(2000);
        WebElement countryMenu = driver.findElement(By.className("fx-flyin--is-active"));
        Assert.assertEquals(countryMenu.getAttribute("aria-hidden"), "false");
        System.out.println("The menu is open.");
        WebElement chooseCountryButton = driver.findElement(By.xpath("//div[@class='country-selection']//div[2]"));
        chooseCountryButton.click(); //sleep(2000);
        WebElement countrySelectedSymbol = driver.findElement(By.xpath("//div[@class=\"fx-grid countries\"]/div[2]/*[name()='svg']/*[name()='use']"));
        Assert.assertTrue(countrySelectedSymbol.isDisplayed());
        System.out.println("The country 'United Kingdom' has been selected.");
        WebElement chooseLanguageButton = driver.findElement(By.xpath("//*[@id=\"js-shop-selection\"]/div/div[2]/div/div[5]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", chooseLanguageButton);
        chooseLanguageButton.click(); //sleep(2000);
        WebElement languageSelectedSymbol = driver.findElement(By.xpath("//div[@class=\"language-selection\"]/div/div[5]/*[name()='svg']/*[name()='use']"));
        Assert.assertTrue(languageSelectedSymbol.isDisplayed());
        System.out.println("The language 'Italiano' has been selected.");

        Select dropdown = new Select(driver.findElement(By.xpath("//*[@id=\"js-shop-selection\"]/div/div[3]/div/select")));
        dropdown.selectByValue("74");
        WebElement option = driver.findElement(By.xpath("//option[31]"));
        Assert.assertTrue(option.isSelected());
        System.out.println("The currency 'JPY · ¥ · Japan, Yen' has been selected.");
        //sleep(2000);

        WebElement saveButton = driver.findElement(By.xpath("//*[@id=\"js-shop-selection\"]/div/div[4]/button"));
        saveButton.click();
        sleep(4000);
        Assert.assertNotEquals(url, driver.getCurrentUrl());
        System.out.println("A new page with the selections made has been entered.");

//        WebElement cookiePopup = driver.findElement(By.className("js-decline-all-cookies"));
//        if(cookiePopup.is) {
//            cookiePopup.click();
//            sleep(2000);
//        }
//        else {
//            System.out.println("Consent popup is not displayed");
//        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement cookiesButton = driver.findElement(By.className("js-decline-all-cookies"));
            wait.until(ExpectedConditions.elementToBeClickable(cookiesButton));
            if(cookiesButton.isDisplayed())
                consentPopup++;
            cookiesButton.click();
            sleep(2000);
        } catch (Exception e) {
            System.out.println("Consent popup was not displayed.");
            Assert.assertEquals(consentPopup, 0);
        }
        finally {
            if(consentPopup>0) {
                System.out.println("Consent popup was closed.");
                Assert.assertTrue(consentPopup > 0);
            }
            WebElement languageAndCurrencySelected = driver.findElement(By.xpath("//div[@role=\"banner\"]/div[3]/div/div[1]"));
            Assert.assertTrue(languageAndCurrencySelected.getText().contains(languageSelected));
            Assert.assertTrue(languageAndCurrencySelected.getText().contains(currencySelected));
            WebElement countryFlagSelected = driver.findElement(By.className("shop-country__flag"));
            Assert.assertEquals(countryFlagSelected.getAttribute("src"), countrySelected);
            System.out.println("All the chosen options have been applied and are now displayed on the page.");
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
