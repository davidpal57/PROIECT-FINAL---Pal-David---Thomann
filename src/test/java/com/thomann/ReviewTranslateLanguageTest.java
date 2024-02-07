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

public class ReviewTranslateLanguageTest {
    WebDriver driver;
    String url = "https://www.thomann.de/ro/index.html";
    //in caz ca nu functioneaza
//    String url = "https://www.thomann.de/ro/kawai_es_120_b.htm";
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
    @Parameters({"revertTranslationP"})
    public void cosProduse(String revertTranslation) {
        Assert.assertEquals(driver.getCurrentUrl(), url);
        System.out.println("The application is open.");
        Assert.assertTrue(consentPopup>0);
        System.out.println("Consent popup was closed.");
        consentPopup--;
        WebElement mostSelledBrandProducts = driver.findElement(By.xpath("//li[@data-identifier=\"tab-topseller-tab-2\"]/a"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1400)", "");
        sleep(1000);
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
            WebElement reviewsPageButton = driver.findElement(By.xpath("//div[@class=\"product-reviews-detail-teaser\"]/div[2]/a"));
            new Actions(driver)
                    .scrollToElement(reviewsPageButton);
            Assert.assertTrue(reviewsPageButton.isDisplayed());
            System.out.println("Page has been scrolled down.");
            reviewsPageButton.click();
            sleep(1000);
            Assert.assertTrue(driver.getCurrentUrl().contains("reviews"));
            System.out.println("The requested page has been opened.");
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
//                JavascriptExecutor js = (JavascriptExecutor) driver; //in caz ca nu functioneaza
                js.executeScript("window.scrollBy(0,400)", "");
                WebElement dropdownButtonClick = driver.findElement(By.xpath("//form[@class=\"js-reviews-form\"]/div/div[3]"));
                Assert.assertTrue(dropdownButtonClick.isDisplayed());
                System.out.println("Page has been scrolled down.");
                dropdownButtonClick.click();
                sleep(1000);
                WebElement dropdownLanguageSelect = driver.findElement(By.xpath("//form[@class=\"js-reviews-form\"]/div/div[3]/div/div[2]/div[2]/div/div[2]"));
                dropdownLanguageSelect.click();
                sleep(1000);
                WebElement selectedLanguage = driver.findElement(By.xpath("//div[@class=\"filters js-reviews-filters\"]/div[3]/select/option[1]"));
                WebElement selectedLanguageFlag = driver.findElement(By.xpath("//div[@class=\"filters js-reviews-filters\"]/div[3]/div/div[2]/img"));
                Assert.assertTrue(selectedLanguageFlag.getAttribute("src").contains(selectedLanguage.getAttribute("data-flag")));
                System.out.println("The filter has been applied and the reviews are now shown in the selected language.");
                WebElement translateReviewToselectedLanguageButton = driver.findElement(By.xpath("//div[@class=\"product-reviews-content__reviews js-reviews\"]/div[1]/div[1]/div/div[1]/a"));
                translateReviewToselectedLanguageButton.click();
                sleep(1000);
                WebElement selectedLanguageFlagReview = driver.findElement(By.xpath("//div[@class=\"product-reviews-content__reviews js-reviews\"]/div[1]/div[1]/div/img[2]"));
                Assert.assertTrue(selectedLanguageFlagReview.isDisplayed());
                System.out.println("The review has been translated to the language that the user chose to browse the website.");
                WebElement revertReviewTranslationButton = driver.findElement(By.xpath("//div[@class=\"product-reviews-content__reviews js-reviews\"]/div[1]/div[1]/div/div[2]/a"));
                Assert.assertTrue(revertReviewTranslationButton.getText().contains(revertTranslation));
                System.out.println("The 'Arată traducerea' button has been replaced with 'Arată originalul' button.");
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
