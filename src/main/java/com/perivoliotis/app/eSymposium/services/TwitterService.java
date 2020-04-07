package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import com.perivoliotis.app.eSymposium.exceptions.SocialMediaInformationNotRetrieved;
import com.perivoliotis.app.eSymposium.integration.clients.TwitterClient;
import com.perivoliotis.app.eSymposium.repos.UserTweetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

@Component
public class TwitterService {

    Logger logger = LoggerFactory.getLogger(TwitterService.class);

    @Autowired
    UserTweetsRepository userTweetsRepository;

    @Autowired
    TwitterClient twitterClient;

    public void fetchAndStoreAllUserTweets(SymposiumUserDTO aUser) throws SocialMediaInformationNotRetrieved {

        try {
            UserTweets usersTweets = twitterClient.getAllTweetsFromUser(aUser.getTwitterUsername());
            logger.debug("Successfully received tweets for user {}", aUser.getSymposiumUsername());
            userTweetsRepository.saveOrUpdate(usersTweets);
            logger.debug("Database successfully updated");
        } catch (TwitterException tex) {
            logger.error("An error occurred while trying to receive tweets of user {}", aUser.getSymposiumUsername());
            throw new SocialMediaInformationNotRetrieved(tex.getMessage());
        }
    }

}
