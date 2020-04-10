package com.perivoliotis.app.eSymposium.integration.clients;

import com.perivoliotis.app.eSymposium.entities.facebook.FacebookPost;
import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.exceptions.FacebookScrapperError;
import com.perivoliotis.app.eSymposium.integration.utilities.FacebookPageScrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class FacebookClient {

    Logger logger = LoggerFactory.getLogger(FacebookClient.class);

    @Value("${facebook.scrapping.scrolls}")
    int maxScrolls;

    @Autowired
    FacebookPageScrapper fbSrcapper;

    public UserPosts getAllFbPostsFromUser(String username) throws FacebookScrapperError {

        try {
            UserPosts userPosts = new UserPosts();

            fbSrcapper.goToFbPageInfo(username);
            userPosts.setFbUser(fbSrcapper.fetchFbUser());
            fbSrcapper.goToFbPagePosts(username);

            Set<FacebookPost> allUserPosts = new HashSet<>();
            logger.debug("Start scrapping for user {}", username);
            int scrolls = 0;

            while (scrolls < maxScrolls){
                fbSrcapper.fetchFbUserPosts(allUserPosts);
                fbSrcapper.scrollDownPage();
                Thread.sleep(6000);
                ++scrolls;
                logger.debug(String.format("Ready for scroll: %s with total posts parsed: %d", scrolls, allUserPosts.size()));
            }

            userPosts.setFacebookPosts(allUserPosts);
            return userPosts;
        } catch (Exception ex) {
            throw new FacebookScrapperError(ex.getMessage());
        } finally {
            fbSrcapper.goToFbHomepage();
        }

    }

}
