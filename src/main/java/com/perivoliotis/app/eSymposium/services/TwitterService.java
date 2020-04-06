package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.entities.twitter.Tweet;
import com.perivoliotis.app.eSymposium.entities.twitter.TwitterUser;
import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import com.perivoliotis.app.eSymposium.integration.clients.TwitterClient;
import com.perivoliotis.app.eSymposium.repos.UserTweetsRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterService {

    @Autowired
    UserTweetsRepository userTweetsRepository;

    @Autowired
    TwitterClient twitterClient;

    public void fetchAndStoreAllUserTweets(TwitterUser aUser) throws Exception {

        UserTweets usersTweets = twitterClient.getAllTweetsFromUser(aUser.getUsername());

        System.out.println("Showing home timeline.");
        for (Tweet tweet : usersTweets.getTweets()) {
            System.out.println(tweet.getText());
        }

        if (userTweetsRepository.saveOrUpdate(usersTweets)) {
            System.out.println("Database successfully updated");
        } else {
            System.out.println("Some error occurred while trying to update database");
        }

    }
}
