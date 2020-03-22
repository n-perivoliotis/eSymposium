package com.perivoliotis.app.eSymposium.services;

import com.mongodb.MongoClient;
import com.perivoliotis.app.eSymposium.DAOs.SymposiumUserDAO;
import com.perivoliotis.app.eSymposium.DAOs.UserPostsDAO;
import com.perivoliotis.app.eSymposium.DAOs.UserTweetsDAO;
import com.perivoliotis.app.eSymposium.entities.FacebookUser;
import com.perivoliotis.app.eSymposium.entities.SymposiumUser;
import com.perivoliotis.app.eSymposium.entities.TwitterUser;
import org.mongodb.morphia.Morphia;

import java.util.List;

public class SymposiumService {

    public void retrieveAndSaveSymposiumUser(){

        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        UserPostsDAO userPostsDAO = new UserPostsDAO(new Morphia(), mongo, "eSymposiumDb");

        UserTweetsDAO userTweetsDAO = new UserTweetsDAO(new Morphia(), mongo, "eSymposiumDb");

        SymposiumUserDAO symposiumUserDAO = new SymposiumUserDAO(new Morphia(), mongo, "eSymposiumDb");

        List<FacebookUser> fbUsers = userPostsDAO.findAllFbUsers();

        List<TwitterUser> twitterUsers = userTweetsDAO.findAllTwitterUsers();

        for (FacebookUser fbUser : fbUsers) {

            SymposiumUser symposiumUser = new SymposiumUser();

            for (TwitterUser twitterUser : twitterUsers) {

                if (twitterUser.getProfileName().equals(fbUser.getPageName())) {

                    symposiumUser.setTwitterUser(twitterUser);
                    symposiumUser.setFacebookUser(fbUser);
                    symposiumUser.setSymposiumUsername(twitterUser.getProfileName());

                    if (symposiumUserDAO.saveOrUpdate(symposiumUser)) {
                        System.out.println("Database successfully updated");
                    } else {
                        System.out.println("Some error occurred while trying to update database");
                    }
                }
            }
        }
    }
}
