package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import com.perivoliotis.app.eSymposium.exceptions.InvalidDatabaseState;
import com.perivoliotis.app.eSymposium.exceptions.SocialMediaInformationNotRetrieved;
import com.perivoliotis.app.eSymposium.exceptions.UserAlreadyExists;
import com.perivoliotis.app.eSymposium.repos.SymposiumUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserManagementService {

    @Autowired
    SymposiumUserRepository symposiumUserRepository;

    @Autowired
    SymposiumService symposiumService;

    @Autowired
    FacebookService fbService;

    @Autowired
    TwitterService twitterService;

    public List<SymposiumUserDTO> fetchAllUsers() {

        List<SymposiumUser> sUsers = symposiumUserRepository.findAll();

        return sUsers.stream()
                .map(sUser -> new SymposiumUserDTO(sUser.getSymposiumUsername(), sUser.getFacebookUser().getUsername(), sUser.getTwitterUser().getUsername()))
                .collect(Collectors.toList());
    }

    public void persistUser(SymposiumUserDTO user) throws UserAlreadyExists{

        try {
            symposiumUserRepository.save(user.asEntity());
        } catch (DuplicateKeyException rex) {
            throw new UserAlreadyExists(rex.getMessage());
        }

    }

    public void synchronizeUser(String username) throws SocialMediaInformationNotRetrieved {

        SymposiumUserDTO user = findSymposiumUser(username);

        //TODO revisit here and tests
        twitterService.fetchAndStoreAllUserTweets(user);
        fbService.fetchAndStoreAllUserPosts(user);
        symposiumService.retrieveAndSaveSymposiumUser();
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
