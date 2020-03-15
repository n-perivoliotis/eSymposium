package com.perivoliotis.app.eSymposium.core.utilities;

import com.perivoliotis.app.eSymposium.core.entities.FacebookPost;
import com.perivoliotis.app.eSymposium.core.entities.FacebookUser;
import com.perivoliotis.app.eSymposium.core.entities.UserPosts;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacebookUtils {

    public static UserPosts getAllFbPostsFromUser(String username) throws Exception{


        UserPosts userPosts = new UserPosts();

        FacebookPageScrapper fbSrcapper = new FacebookPageScrapper();

        fbSrcapper.initializeWebDriver();
        String email = ConfigurationUtils.readFromProperties("login.facebook.personal.account.email");
        String password = ConfigurationUtils.readFromProperties("login.facebook.personal.account.password");
        fbSrcapper.goToFbHomepage(email, password);

        fbSrcapper.goToFbPageInfo(username);

        userPosts.setFbUser(fetchFbUser(fbSrcapper.getDriver()));

        fbSrcapper.goToFbPagePosts(username);

        Set<FacebookPost> allUserPosts = new HashSet<>();

        System.out.println("User " + username);

        int scrolls = 0;

        while (scrolls < 20){

            List<WebElement> posts = fbSrcapper.extractOncePosts();

            for(WebElement post:posts){
                allUserPosts.add(FacebookUtils.toPost(post));
            }
            fbSrcapper.scrollDownPage();
            Thread.sleep(6000);
            ++scrolls;
            System.out.println("Ready for scroll: " + scrolls + " with total posts parsed: " + allUserPosts.size());
        }

        fbSrcapper.closeBrowser();

        userPosts.setFacebookPosts(allUserPosts);

        return userPosts;
    }

    public static FacebookUser fetchFbUser(WebDriver driver) throws Exception{

        FacebookUser fbUser = new FacebookUser();

        WebElement commonWebElement;

        commonWebElement = driver.findElement(By.id("seo_h1_tag"));
        fbUser.setPageName(commonWebElement.getText());

        commonWebElement = driver.findElement(By.id("u_0_y"));
        fbUser.setUsername(commonWebElement.getText());

        commonWebElement = driver.findElement(By.xpath("//div[@class='_50f4'][contains(text(),'Πληροφορίες')]/following-sibling::*"));
        fbUser.setDescription(commonWebElement.getText());

        commonWebElement = driver.findElement(By.className("_6tay"));
        fbUser.setProfilePictureUrl(commonWebElement.findElement(By.tagName("img")).getAttribute("src"));


        commonWebElement = driver.findElement(By.className("_4on8"));
        fbUser.setBackgroundPictureUrl(commonWebElement.findElement(By.tagName("img")).getAttribute("src"));


        return fbUser;
    }

    public static FacebookPost toPost(WebElement postHtml) throws Exception{

        FacebookPost fbPost = new FacebookPost();

        WebElement commonWebElement;

        commonWebElement = postHtml.findElement(By.className("userContent"));
        fbPost.setDescriptionText(commonWebElement.getText());

        commonWebElement = postHtml.findElement(By.className("_1g5v"));

        fbPost.setReactions(uiNumberToInt(commonWebElement.getText()));

        commonWebElement = postHtml.findElement(By.className("_36_q"));
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

    private static int uiNumberToInt(String reac){

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

    private static String keepUsefulInfo(String reac){

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

    private static String removeComments(String commentsNum){

        if(commentsNum.contains("σχόλια")){
            return commentsNum.replaceAll("σχόλια", "");
        }else if(commentsNum.contains("σχόλιο")){
            return commentsNum.replaceAll("σχόλιο", "");
        }else {
            return commentsNum;
        }

    }

    private static LocalDateTime toDate(String dateStr) {

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

    private static List<String> findResource(List<WebElement> elements, String attribute){

        List<String> resource = new ArrayList<>();

        for(WebElement element:elements){
            resource.add(element.getAttribute(attribute));
        }

        return resource;
    }


}
