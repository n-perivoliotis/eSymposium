package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.entities.facebook.FacebookUser;
import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import com.perivoliotis.app.eSymposium.entities.twitter.TwitterUser;
import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import com.perivoliotis.app.eSymposium.exceptions.UserAlreadyExists;
import com.perivoliotis.app.eSymposium.repos.SymposiumUserRepository;
import com.perivoliotis.app.eSymposium.repos.UserPostsRepository;
import com.perivoliotis.app.eSymposium.repos.UserTweetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SymposiumService {

    Logger logger = LoggerFactory.getLogger(SymposiumService.class);

    @Autowired
    UserPostsRepository userPostsRepository;

    @Autowired
    UserTweetsRepository userTweetsRepository;

    @Autowired
    SymposiumUserRepository symposiumUserRepository;

    public void persistUser(SymposiumUserDTO user) {

        try {
            symposiumUserRepository.save(user.asEntity());
        } catch (DuplicateKeyException rex) {
            throw new UserAlreadyExists(rex.getMessage());
        }

    }

    public SymposiumUserDTO findUser(String username) {

        List<SymposiumUser> userList = symposiumUserRepository.findBySymposiumUsername(username);

        SymposiumUserDTO result = new SymposiumUserDTO();
        result.setSymposiumUsername(userList.get(0).getSymposiumUsername());
        result.setFbUsername(userList.get(0).getFacebookUser().getUsername());
        result.setTwitterUsername(userList.get(0).getTwitterUser().getUsername());

        return result;
    }

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
                    symposiumUserRepository.saveOrUpdate(symposiumUser);
                    logger.debug("Database successfully updated");
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
