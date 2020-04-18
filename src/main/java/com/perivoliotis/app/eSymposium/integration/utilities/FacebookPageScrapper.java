package com.perivoliotis.app.eSymposium.integration.utilities;

import com.perivoliotis.app.eSymposium.entities.facebook.FacebookPost;
import com.perivoliotis.app.eSymposium.entities.facebook.FacebookUser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FacebookPageScrapper {

    @Value("${webdriver.chrome.driver}")
    private String chromeDriverPath;

    @Value("${login.facebook.personal.account.email}")
    private String email;

    @Value("${login.facebook.personal.account.password}")
    private String password;

    private WebDriver driver;

    @PostConstruct
    public void initializeWebDriver(){

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("headless");

        Properties props = System.getProperties();
        props.setProperty("webdriver.chrome.driver", chromeDriverPath);

        this.driver = new ChromeDriver(options);

        goToFbHomepageAndLogin(email, password);
    }

    public void goToFbHomepage() {
        driver.navigate().to("https://www.facebook.com/");
    }

    public void goToFbHomepageAndLogin(String email, String password) {

        goToFbHomepage();
        driver.manage().window().maximize();

        WebElement emailEditbox = driver.findElement(By.id("email"));
        WebElement passwordEditbox = driver.findElement(By.id("pass"));
        WebElement submitButton = driver.findElement(By.id("loginbutton"));

        emailEditbox.sendKeys(email);
        passwordEditbox.sendKeys(password);
        submitButton.click();
    }

    public void goToFbPagePosts(String username){
        driver.navigate().to("https://www.facebook.com/pg/" + username.replace("@","") + "/posts/");
    }

    public void goToFbPageInfo(String username){
        driver.navigate().to("https://www.facebook.com/pg/" + username.replace("@","") + "/about/");
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

    public FacebookUser fetchFbUser() {

        FacebookUser fbUser = new FacebookUser();

        WebElement commonWebElement;

        commonWebElement = driver.findElement(By.id("seo_h1_tag"));
        fbUser.setPageName(commonWebElement.getText());

        commonWebElement = driver.findElement(By.id("u_0_w"));
        fbUser.setUsername(commonWebElement.getText());

        commonWebElement = driver.findElement(By.xpath("//div[@class='_50f4'][contains(text(),'Πληροφορίες')]/following-sibling::*"));
        fbUser.setDescription(commonWebElement.getText());

        commonWebElement = driver.findElement(By.className("_6tay"));
        fbUser.setProfilePictureUrl(commonWebElement.findElement(By.tagName("img")).getAttribute("src"));

        commonWebElement = driver.findElement(By.className("_4on8"));
        fbUser.setBackgroundPictureUrl(commonWebElement.findElement(By.tagName("img")).getAttribute("src"));

        return fbUser;
    }

    public void fetchFbUserPosts(Set<FacebookPost> allUserPosts) {

        List<WebElement> posts = extractOncePosts();

        for(WebElement post : posts){

            allUserPosts.add(toPost(post));
        }
    }

    private FacebookPost toPost(WebElement postHtml) {

        FacebookPost fbPost = new FacebookPost();

        WebElement commonWebElement;

        commonWebElement = postHtml.findElement(By.className("userContent"));
        fbPost.setDescriptionText(commonWebElement.getText());

        commonWebElement = postHtml.findElement(By.className("_81hb"));

        fbPost.setReactions(uiNumberToInt(commonWebElement.getText()));

        commonWebElement = postHtml.findElement(By.className("_42ft"));
        fbPost.setTotalComments(uiNumberToInt(removeComments(commonWebElement.getText())));

        commonWebElement = postHtml.findElement(By.tagName("abbr"));

        fbPost.setTimestamp(toDate(commonWebElement.getAttribute("title")));

        List<WebElement> images = postHtml.findElements(By.tagName("img"));

        fbPost.setImagesUrls(findResource(images, "src"));

        List<WebElement> videos = postHtml.findElements(By.tagName("video"));

        fbPost.setVideoUrls(findResource(videos, "src"));

        List<WebElement> urls = postHtml.findElements(By.tagName("mbs _6m6 _2cnj _5s6c"));

        fbPost.setOnlineUrls(findResource(urls, "href"));

//        commonWebElement = postHtml.findElement(By.className("text_exposed_link"));
//        fbPost.setLinkToMoreDescription(commonWebElement.getAttribute("href"));

        return fbPost;
    }

    private int uiNumberToInt(String reac) {

        int result = 0;

        reac = reac.trim();

        if (!reac.isEmpty()) {

            reac = keepUsefulInfo(reac);

            if (reac.contains("χιλ.")){
                reac = reac.replaceAll("χιλ.","");
                reac = reac.replaceAll(",",".");
                result = Math.round(Float.parseFloat(reac)*1000);
            }else if (reac.contains("εκ.")){
                reac = reac.replaceAll("εκ.","");
                reac = reac.replaceAll(",",".");
                result = Math.round(Long.parseLong(reac)*1000000);
            }else {
                reac = reac.replaceAll("\\.","");
                result = Integer.parseInt(reac);
            }
        }

        return result;
    }

    private String keepUsefulInfo(String reac) {

        Pattern pattern = Pattern.compile("\\d+ [Α-Ωα-ω]+\\.");
        Matcher matcher = pattern.matcher(reac);

        Pattern simpleNumberPattern = Pattern.compile("\\d+");
        Matcher simpleNumberMatcher = simpleNumberPattern.matcher(reac);

        if (matcher.find()) {
            return matcher.group(0);
        } else if (simpleNumberMatcher.find()){
            return simpleNumberMatcher.group(0);
        }

        return reac;
    }

    private String removeComments(String commentsNum) {

        if(commentsNum.contains("σχόλια")){
            return commentsNum.replaceAll("σχόλια", "");
        }else if(commentsNum.contains("σχόλιο")){
            return commentsNum.replaceAll("σχόλιο", "");
        }else {
            return commentsNum;
        }

    }

    private LocalDateTime toDate(String dateStr) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yy, h:mm a");

        if(dateStr.contains("μ.μ.")){
            dateStr = dateStr.replace("μ.μ.", "PM");
        }else if(dateStr.contains("π.μ.")){
            dateStr = dateStr.replace("π.μ.","AM");
        }

        try {
            return  LocalDateTime.parse(dateStr, formatter);
        }catch (Exception ex){
            return null;
        }
    }

    private List<String> findResource(List<WebElement> elements, String attribute) {

        List<String> resource = new ArrayList<>();

        for(WebElement element:elements){
            resource.add(element.getAttribute(attribute));
        }

        return resource;
    }

}
