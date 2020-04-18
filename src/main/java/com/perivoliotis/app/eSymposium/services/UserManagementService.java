package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.dtos.UserSocialDataDTO;
import com.perivoliotis.app.eSymposium.dtos.facebook.FacebookPostDTO;
import com.perivoliotis.app.eSymposium.dtos.facebook.FacebookUserDTO;
import com.perivoliotis.app.eSymposium.dtos.twitter.TweetDTO;
import com.perivoliotis.app.eSymposium.dtos.twitter.TwitterUserDTO;
import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import com.perivoliotis.app.eSymposium.exceptions.FacebookScrapperError;
import com.perivoliotis.app.eSymposium.exceptions.InvalidDatabaseState;
import com.perivoliotis.app.eSymposium.exceptions.InvalidUserInformation;
import com.perivoliotis.app.eSymposium.exceptions.UserAlreadyExists;
import com.perivoliotis.app.eSymposium.integration.clients.FacebookClient;
import com.perivoliotis.app.eSymposium.integration.clients.TwitterClient;
import com.perivoliotis.app.eSymposium.repos.SymposiumUserRepository;
import com.perivoliotis.app.eSymposium.repos.UserPostsRepository;
import com.perivoliotis.app.eSymposium.repos.UserTweetsRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
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

    ModelMapper modelMapper;

    @PostConstruct
    public void onInit() {
        modelMapper = new ModelMapper();
    }

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

    public void synchronizeUser(String username) throws TwitterException, FacebookScrapperError, InvalidUserInformation {

        SymposiumUserDTO user = findSymposiumUser(username);

        if (user.isValid()) {
            UserTweets usersTweets = twitterClient.getAllTweetsFromUser(user.getTwitterUsername());
            logger.debug("Successfully received tweets for user {}", user.getSymposiumUsername());
            userTweetsRepository.saveOrUpdate(usersTweets);
            logger.debug("Database successfully updated");

            UserPosts usersPosts = facebookClient.getAllFbPostsFromUser(user.getFbUsername());
            logger.debug("Successfully received posts for user {}", user.getSymposiumUsername());
            userPostsRepository.saveOrUpdate(usersPosts);
            logger.debug("Database successfully updated");
        } else {
            throw new InvalidUserInformation(String.format("Symposium User retrieved for %s contains invalid information ", username));
        }

    }

    public UserSocialDataDTO displayUser(String username) {

        SymposiumUserDTO user = findSymposiumUser(username);

        List<UserPosts> posts = userPostsRepository.findByUsername(user.getFbUsername());
        List<UserTweets> tweets = userTweetsRepository.findByUsername(user.getTwitterUsername());

        UserSocialDataDTO userSocialDataDTO = new UserSocialDataDTO();

        userSocialDataDTO.setSymposiumUsername(username);
        userSocialDataDTO.setFacebookUser(fbUserAsDTO(posts));
        userSocialDataDTO.setFbPosts(facebookPostAsDTO(posts));
        userSocialDataDTO.setTweets(tweetAsDTO(tweets));
        userSocialDataDTO.setTwitterUser(twitterUserAsDTO(tweets));

        return userSocialDataDTO;
    }

    FacebookUserDTO fbUserAsDTO(List<UserPosts> posts) {

        if (posts.size() == 1) {
            return modelMapper.map(posts.get(0).getFbUser(), FacebookUserDTO.class);
        } else {
            throw new InvalidDatabaseState(String.format("A user should have only one UserPosts object but found %d", posts.size()));
        }
    }

    TwitterUserDTO twitterUserAsDTO(List<UserTweets> tweets) {

        if (tweets.size() == 1) {
            return modelMapper.map(tweets.get(0).getAUser(), TwitterUserDTO.class);
        } else {
            throw new InvalidDatabaseState(String.format("A user should have only one UserTweets object but found %d", tweets.size()));
        }
    }

    Set<FacebookPostDTO> facebookPostAsDTO(List<UserPosts> posts) {

        if (posts.size() == 1) {
            return posts.get(0).getFacebookPosts().stream()
                    .map(post -> modelMapper.map(post, FacebookPostDTO.class))
                    .collect(Collectors.toSet());
        } else {
            throw new InvalidDatabaseState(String.format("A user should have only one UserPosts object but found %d", posts.size()));
        }
    }

    Set<TweetDTO> tweetAsDTO(List<UserTweets> tweets) {

        if (tweets.size() == 1) {
            return tweets.get(0).getTweets().stream()
                    .map(post -> modelMapper.map(post, TweetDTO.class))
                    .collect(Collectors.toSet());
        } else {
            throw new InvalidDatabaseState(String.format("A user should have only one UserTweets object but found %d", tweets.size()));
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
