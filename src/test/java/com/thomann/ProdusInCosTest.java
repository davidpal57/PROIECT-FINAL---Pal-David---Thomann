package com.thomann;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class ProdusInCosTest {
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
    @Parameters({"basketAmountP", "basketNotificationTxtP"})
    public void cosProduse(int basketAmount, String basketNotificationTxt) {
        Assert.assertEquals(driver.getCurrentUrl(), url);
        System.out.println("The application is open.");
        Assert.assertTrue(consentPopup>0);
        System.out.println("Consent popup was closed.");
        consentPopup--;
        WebElement mostSelledBrandProducts = driver.findElement(By.xpath("//li[@data-identifier=\"tab-topseller-tab-2\"]/a"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1400)", "");
        WebElement visibileProduct = driver.findElement(By.xpath("//div[@id=\"prod-highlights-tabbed\"]/div[2]"));
        Assert.assertTrue(visibileProduct.isDisplayed());
        System.out.println("Page has been scrolled down.");
        mostSelledBrandProducts.click();
        sleep(1000);
        System.out.println("A new list of products has been displayed.");
        WebElement firstProductDisplayed = driver.findElement(By.xpath("//div[@id=\"fx-product-carousel-tabbed-topseller-tab-2\"]/div/div[2]/div/div[1]/div/div[1]/div[1]"));
        firstProductDisplayed.click();
        sleep(1000);
        Assert.assertNotEquals(driver.getCurrentUrl(), url);
        System.out.println("The request page has been opened.");
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
            Assert.assertEquals(consentPopup, 0);
        }
        finally {
            if(consentPopup>0) {
                System.out.println("Consent popup was closed.");
                Assert.assertTrue(consentPopup > 0);
                consentPopup--;
            }
            WebElement nameOfProduct = driver.findElement(By.className("product-title__title"));
            String productName = nameOfProduct.getText();
            WebElement addInBasketButton = driver.findElement(By.className("call-to-action__action"));
            addInBasketButton.click(); //basketAmount++;
            sleep(1500);
            Assert.assertEquals(driver.getCurrentUrl(), "https://www.thomann.de/ro/basket.html");
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
                Assert.assertEquals(consentPopup, 0);
            }
            finally {
                if(consentPopup>0) {
                    System.out.println("Consent popup was closed.");
                    Assert.assertTrue(consentPopup > 0);
                }
//                String basketAmountTxt = Integer.toString(basketAmount);
                WebElement productInBasketNotification = driver.findElement(By.xpath("//*[@id=\"notifications-display\"]/div/div/div/div[2]"));
                Assert.assertTrue(productInBasketNotification.getText().contains(productName + basketNotificationTxt));
                System.out.println("The notification: " + "'" + productName + basketNotificationTxt + "'" + " has been displayed.");
//                WebElement linkToProductInBasket = driver.findElement(By.className("article-information"));
//                Assert.assertTrue(linkToProductInBasket.getText().contains(productName));
//                WebElement numberOfPorductsInBasket = driver.findElement(By.className("user-navigation__basket-amount"));
//                Assert.assertTrue(numberOfPorductsInBasket.getText().contains(basketAmountTxt));
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
