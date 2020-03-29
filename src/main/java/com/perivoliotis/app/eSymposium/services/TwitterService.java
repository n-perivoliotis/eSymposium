package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.entities.twitter.Tweet;
import com.perivoliotis.app.eSymposium.entities.twitter.TwitterUser;
import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import com.perivoliotis.app.eSymposium.repos.UserTweetsRepository;
import com.perivoliotis.app.eSymposium.utilities.TwitterUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TwitterService {

    @Autowired
    UserTweetsRepository userTweetsRepository;

    public void fetchAndStoreAllUsersTweets(List<TwitterUser> users) throws Exception{

        for (TwitterUser aUser : users){

            UserTweets usersTweets = TwitterUtils.getAllTweetsFromUser(aUser.getUsername());

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
}
