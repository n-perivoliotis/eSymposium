package com.perivoliotis.app.eSymposium.core.services;

import com.mongodb.MongoClient;
import com.perivoliotis.app.eSymposium.core.DAOs.UserTweetsDAO;
import com.perivoliotis.app.eSymposium.core.utilities.TwitterUtils;
import com.perivoliotis.app.eSymposium.core.entities.Tweet;
import com.perivoliotis.app.eSymposium.core.entities.TwitterUser;
import com.perivoliotis.app.eSymposium.core.entities.UserTweets;
import org.mongodb.morphia.Morphia;
import java.util.List;

public class TwitterService {

    public void fetchAndStoreAllUsersTweets(List<TwitterUser> users) throws Exception{

        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        UserTweetsDAO userTweetsDAO = new UserTweetsDAO(new Morphia(), mongo, "eSymposiumDb");

        for (TwitterUser aUser : users){

            UserTweets usersTweets = TwitterUtils.getAllTweetsFromUser(aUser.getUsername());

            System.out.println("Showing home timeline.");
            for (Tweet tweet : usersTweets.getTweets()) {
                System.out.println(tweet.getText());
            }

            if(userTweetsDAO.saveOrUpdate(usersTweets)){
                System.out.println("Database successfully updated");
            }else{
                System.out.println("Some error occurred while trying to update database");
            }
        }

    }
}
