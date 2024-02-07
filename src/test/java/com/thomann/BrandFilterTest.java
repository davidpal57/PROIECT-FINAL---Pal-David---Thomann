package com.thomann;

import org.openqa.selenium.By;
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

public class BrandFilterTest {
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
    @Parameters({"brandFilterP1", "brandFilterP2"})
    public void filtrareBrand(String brandFilter1, String brandFilter2) {
        Assert.assertEquals(driver.getCurrentUrl(), url);
        System.out.println("The application is open.");
        Assert.assertTrue(consentPopup>0);
        System.out.println("Consent popup was closed.");
        consentPopup--;
        WebElement navigationMenuButton = driver.findElement(By.className("fx-size--medium"));
        navigationMenuButton.click();
        WebElement navigationMenu = driver.findElement(By.id("fx-flyin-main"));
        Assert.assertEquals(navigationMenu.getAttribute("aria-hidden"),"false");
        System.out.println("The navigation menu is now open.");
        sleep(1000);
        WebElement navigationKeysPage = driver.findElement(By.xpath("//*[@id=\"drilldown-menu-productCategories\"]/ul/li[3]/a"));
        navigationKeysPage.click();
        sleep(1500);
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.thomann.de/ro/claviaturi1.html");
        System.out.println("The requested page has been opened");
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
            Assert.assertEquals(consentPopup, 0);
        }
        finally {
            if(consentPopup>0) {
                System.out.println("Consent popup was closed.");
                Assert.assertTrue(consentPopup > 0);
                consentPopup--;
            }
            WebElement grandPianoPage = driver.findElement(By.xpath("//a[@title='Piane de Concert']"));
            new Actions(driver)
                    .scrollToElement(grandPianoPage);
            grandPianoPage.click();
            Assert.assertEquals(driver.getCurrentUrl(), "https://www.thomann.de/ro/piane_de_concert.html");
            System.out.println("The requested page has been opened.");
            sleep(1000);
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
                WebElement showMoreButton = driver.findElement(By.xpath("//div[@class='fx-collapsible__trigger']"));
                showMoreButton.click();
                WebElement showLessButton = driver.findElement(By.xpath("//div[@class=\"fx-collapsible fx-collapsible--open\"]/div[2]/span"));
                Assert.assertTrue(showLessButton.getText().contains("ARATĂ MAI PUȚIN"));
                System.out.println("All available product brands are shown.");
                sleep(1000);
                WebElement brandSelectCheckbox = driver.findElement(By.xpath("//*[@id=\"Producător\"]/div/div[5]"));
                brandSelectCheckbox.click();
                sleep(1000);
                Assert.assertTrue(driver.getCurrentUrl().contains("Steinway"));
                System.out.println("The products have been filtered and the name of the brand selected can be found in the current link.");
                WebElement brandFilterBox = driver.findElement(By.xpath("//div[@class='filter-chips filter-chips--large']/div/div/div[1]"));
                Assert.assertTrue(brandFilterBox.getText().contains(brandFilter1));
                Assert.assertTrue(brandFilterBox.getText().contains(brandFilter2));
                System.out.println("The filter box can be found above the first product containing the name of the brand selected.");
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
