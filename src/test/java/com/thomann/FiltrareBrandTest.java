package com.thomann;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class FiltrareBrandTest {
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
    @Parameters({"brandFilterP1", "brandFilterP2"})
    public void filtrareBrand(String brandFilter1, String brandFilter2) {
        int consentPopup = 0;
        WebElement navigationMenu = driver.findElement(By.className("fx-size--medium"));
        navigationMenu.click();
        sleep(1000);
        WebElement navigationKeysPage = driver.findElement(By.xpath("//*[@id=\"drilldown-menu-productCategories\"]/ul/li[3]/a"));
        navigationKeysPage.click();
        sleep(1000);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
            WebElement cookiesButton = driver.findElement(By.className("js-decline-all-cookies"));
            wait.until(ExpectedConditions.elementToBeClickable(cookiesButton));
            if (cookiesButton.isDisplayed())
                consentPopup++;
            cookiesButton.click();
            sleep(4000);
        } catch (Exception e) {
            System.out.println("Consent popup was not displayed.");
        }
        finally{
            if (consentPopup>0)
                System.out.println("Consent popup was closed.");
            WebElement grandPianoPage = driver.findElement(By.xpath("//a[@title='Piane de Concert']"));
            new Actions(driver)
                    .scrollToElement(grandPianoPage);
            grandPianoPage.click();
            sleep(2000);
        }
        WebElement showMoreButton = driver.findElement(By.xpath("//div[@class='fx-collapsible__trigger']"));
        showMoreButton.click();
        sleep(2000);
        WebElement brandSelectCheckbox = driver.findElement(By.xpath("//*[@id=\"Producător\"]/div/div[5]"));
        brandSelectCheckbox.click();
        sleep(2000);
        WebElement brandFilterBox = driver.findElement(By.xpath("//div[@class='filter-chips filter-chips--large']/div/div/div[1]"));
        Assert.assertTrue(brandFilterBox.getText().contains(brandFilter1));
        Assert.assertTrue(brandFilterBox.getText().contains(brandFilter2));
        sleep(2000);
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
