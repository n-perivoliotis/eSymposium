package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.exceptions.FacebookScrapperError;
import com.perivoliotis.app.eSymposium.exceptions.SocialMediaInformationNotRetrieved;
import com.perivoliotis.app.eSymposium.integration.clients.FacebookClient;
import com.perivoliotis.app.eSymposium.repos.UserPostsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FacebookService {

    Logger logger = LoggerFactory.getLogger(FacebookService.class);

    @Autowired
    UserPostsRepository userPostsRepository;

    @Autowired
    FacebookClient facebookClient;

    public void fetchAndStoreAllUserPosts(SymposiumUserDTO aUser) throws SocialMediaInformationNotRetrieved {

        try {
            UserPosts usersPosts = facebookClient.getAllFbPostsFromUser(aUser.getFbUsername());
            logger.debug("Successfully received posts for user {}", aUser.getSymposiumUsername());
            userPostsRepository.saveOrUpdate(usersPosts);
            logger.debug("Database successfully updated");
        } catch (FacebookScrapperError ex) {
            logger.error("An error occurred while trying to receive posts of user {}", aUser.getSymposiumUsername());
            throw new SocialMediaInformationNotRetrieved(ex.getMessage());
        }
    }

}
