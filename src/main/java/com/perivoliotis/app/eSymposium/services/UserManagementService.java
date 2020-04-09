package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import com.perivoliotis.app.eSymposium.exceptions.FacebookScrapperError;
import com.perivoliotis.app.eSymposium.exceptions.InvalidDatabaseState;
import com.perivoliotis.app.eSymposium.exceptions.SocialMediaInformationNotRetrieved;
import com.perivoliotis.app.eSymposium.exceptions.UserAlreadyExists;
import com.perivoliotis.app.eSymposium.integration.clients.FacebookClient;
import com.perivoliotis.app.eSymposium.integration.clients.TwitterClient;
import com.perivoliotis.app.eSymposium.repos.SymposiumUserRepository;
import com.perivoliotis.app.eSymposium.repos.UserPostsRepository;
import com.perivoliotis.app.eSymposium.repos.UserTweetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserManagementService {

    Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    @Autowired
    SymposiumUserRepository symposiumUserRepository;

    @Autowired
    UserTweetsRepository userTweetsRepository;

    @Autowired
    UserPostsRepository userPostsRepository;

    @Autowired
    FacebookClient facebookClient;

    @Autowired
    TwitterClient twitterClient;

    public List<SymposiumUserDTO> fetchAllUsers() {

        List<SymposiumUser> sUsers = symposiumUserRepository.findAll();

        return sUsers.stream()
                .map(sUser -> new SymposiumUserDTO(sUser.getSymposiumUsername(), sUser.getFacebookUser().getUsername(), sUser.getTwitterUser().getUsername()))
                .collect(Collectors.toList());
    }

    public void persistUser(SymposiumUserDTO user) throws UserAlreadyExists {

        try {
            symposiumUserRepository.save(user.asEntity());
        } catch (DuplicateKeyException rex) {
            throw new UserAlreadyExists(rex.getMessage());
        }

    }

    public void synchronizeUser(String username) throws SocialMediaInformationNotRetrieved {

        SymposiumUserDTO user = findSymposiumUser(username);

        try {

            UserTweets usersTweets = twitterClient.getAllTweetsFromUser(user.getTwitterUsername());
            logger.debug("Successfully received tweets for user {}", user.getSymposiumUsername());
            userTweetsRepository.saveOrUpdate(usersTweets);
            logger.debug("Database successfully updated");

            UserPosts usersPosts = facebookClient.getAllFbPostsFromUser(user.getFbUsername());
            logger.debug("Successfully received posts for user {}", user.getSymposiumUsername());
            userPostsRepository.saveOrUpdate(usersPosts);
            logger.debug("Database successfully updated");

        } catch (TwitterException tex) {
            logger.error("An error occurred while trying to receive tweets of user {}", user.getSymposiumUsername());
            throw new SocialMediaInformationNotRetrieved(tex.getMessage());
        } catch (FacebookScrapperError ex) {
            logger.error("An error occurred while trying to receive posts of user {}", user.getSymposiumUsername());
            throw new SocialMediaInformationNotRetrieved(ex.getMessage());
        }

    }

    SymposiumUserDTO findSymposiumUser(String username) {

        List<SymposiumUser> userList = symposiumUserRepository.findBySymposiumUsername(username);

        if (userList.size() == 1) {
            SymposiumUserDTO result = new SymposiumUserDTO();
            result.setSymposiumUsername(userList.get(0).getSymposiumUsername());
            result.setFbUsername(userList.get(0).getFacebookUser().getUsername());
            result.setTwitterUsername(userList.get(0).getTwitterUser().getUsername());
            return result;
        } else {
            throw new InvalidDatabaseState(String.format("Duplicate or missing database entry for symposium username %s", username));
        }
    }
}
