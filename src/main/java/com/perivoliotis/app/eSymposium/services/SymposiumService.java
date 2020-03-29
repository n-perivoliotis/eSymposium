package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.entities.facebook.FacebookUser;
import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import com.perivoliotis.app.eSymposium.entities.twitter.TwitterUser;
import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import com.perivoliotis.app.eSymposium.repos.SymposiumUserRepository;
import com.perivoliotis.app.eSymposium.repos.UserPostsRepository;
import com.perivoliotis.app.eSymposium.repos.UserTweetsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SymposiumService {

    @Autowired
    UserPostsRepository userPostsRepository;

    @Autowired
    UserTweetsRepository userTweetsRepository;

    @Autowired
    SymposiumUserRepository symposiumUserRepository;

    public void retrieveAndSaveSymposiumUser(){

        List<FacebookUser> fbUsers = findAllFbUsers();

        List<TwitterUser> twitterUsers = findAllTwitterUsers();

        for (FacebookUser fbUser : fbUsers) {

            SymposiumUser symposiumUser = new SymposiumUser();

            for (TwitterUser twitterUser : twitterUsers) {

                if (twitterUser.getProfileName().equals(fbUser.getPageName())) {

                    symposiumUser.setTwitterUser(twitterUser);
                    symposiumUser.setFacebookUser(fbUser);
                    symposiumUser.setSymposiumUsername(twitterUser.getProfileName());

                    if (symposiumUserRepository.saveOrUpdate(symposiumUser)) {
                        System.out.println("Database successfully updated");
                    } else {
                        System.out.println("Some error occurred while trying to update database");
                    }
                }
            }
        }
    }

    private List<FacebookUser> findAllFbUsers() {

        List<FacebookUser> users = new ArrayList<>();

        List<UserPosts> allPosts = userPostsRepository.findAll();

        for (UserPosts post : allPosts) {
            users.add(post.getFbUser());
        }

        return users;
    }

    private List<TwitterUser> findAllTwitterUsers() {

        List<TwitterUser> users = new ArrayList<>();

        List<UserTweets> allTweets = userTweetsRepository.findAll();

        for (UserTweets tweet : allTweets) {
            users.add(tweet.getAUser());
        }

        return users;
    }

}
