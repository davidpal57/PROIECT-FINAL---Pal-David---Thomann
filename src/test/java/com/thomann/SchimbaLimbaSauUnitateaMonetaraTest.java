//package com.thomann;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.testng.Assert;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Parameters;
//import org.testng.annotations.Test;
//
//public class SchimbaLimbaSauUnitateaMonetaraTest {
//    WebDriver driver;
//    String url = "https://www.thomann.de/ro/index.html";
//    @BeforeTest
//    @Parameters({"browserChoice"})
//    public void setup(String browser){
//        switch(browser){
//            case "chrome": driver = new ChromeDriver(); break;
//            case "firefox": driver = new FirefoxDriver(); break;
//            case "edge" : driver = new EdgeDriver(); break;
//            default: driver = new ChromeDriver();
//        }
//        driver.get(url);
//        driver.manage().window().maximize();
//        WebElement cookiesButton = driver.findElement(By.className("js-decline-all-cookies"));
//        cookiesButton.click();
//        sleep(2000);
//    }
//    @Test
//    @Parameters
//    public void alegeTara() {
//        WebElement butonMeniuTara = driver.findElement(By.className("shop-country"));
//        butonMeniuTara.click();
//        sleep(2000);
//        WebElement butonTara = driver.findElement(By.xpath("//div[@class='country-selection']//div[2]"));
//        butonTara.click();
//        sleep(2000);
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        WebElement butonLimba = driver.findElement(By.linkText("Italiano"));
//        js.executeScript("div[5]")
//
//    }
//    @AfterTest(alwaysRun = true)
//    public void tearDown(){
//        driver.close();
//    }
//    public static void sleep(int milliseconds) {
//        try {
//            Thread.sleep(milliseconds);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class SchimbaLimbaSauUnitateaMonetara {

    WebDriver driver;
    @Test
    public void ByVisibleElement() {
        System.setProperty("webdriver.gecko.driver","D://Selenium Environment//Drivers//geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        //Launch the application
        driver.get("https://www.browserstack.com/guide/selenium-scroll-tutorial");

        //Locating element by link text and store in variable "Element"
        WebElement Element = driver.findElement(By.linkText("Try Selenium Testing For Free"));

        // Scrolling down the page till the element is found
        js.executeScript("arguments[0].scrollIntoView();", Element);
    }
}