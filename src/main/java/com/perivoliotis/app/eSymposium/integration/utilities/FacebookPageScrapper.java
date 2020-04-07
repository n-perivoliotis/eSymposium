package com.perivoliotis.app.eSymposium.integration.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

@Component
public class FacebookPageScrapper {

    @Value("${webdriver.chrome.driver}")
    private String chromeDriverPath;

    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    @PostConstruct
    public void initializeWebDriver(){

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        Properties props = System.getProperties();
        props.setProperty("webdriver.chrome.driver", chromeDriverPath);

        this.driver = new ChromeDriver(options);
    }

    public void goToFbHomepage(String email, String password) {

        driver.navigate().to("https://www.facebook.com/");
        driver.manage().window().maximize();

        WebElement emailEditbox = driver.findElement(By.id("email"));
        WebElement passwordEditbox = driver.findElement(By.id("pass"));
        WebElement submitButton = driver.findElement(By.id("loginbutton"));

        emailEditbox.sendKeys(email);
        passwordEditbox.sendKeys(password);
        submitButton.click();
    }

    public void goToFbPagePosts(String username){
        driver.navigate().to("https://www.facebook.com/pg/" + username + "/posts/");
    }

    public void goToFbPageInfo(String username){
        driver.navigate().to("https://www.facebook.com/pg/" + username + "/about/");
    }

    public List<WebElement> extractOncePosts() {

        return driver.findElements(By.className("userContentWrapper"));
    }

    public void scrollDownPage() {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

//        js.executeScript("window.scrollBy(0,2000)");
    }

    public void closeBrowser() {
        driver.close();
    }

}
