package com.perivoliotis.app.eSymposium.controllers;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.exceptions.SocialMediaInformationNotRetrieved;
import com.perivoliotis.app.eSymposium.exceptions.UserAlreadyExists;
import com.perivoliotis.app.eSymposium.responses.HomePageResponse;
import com.perivoliotis.app.eSymposium.services.FacebookService;
import com.perivoliotis.app.eSymposium.services.HomePageService;
import com.perivoliotis.app.eSymposium.services.SymposiumService;
import com.perivoliotis.app.eSymposium.services.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SymposiumController {

    @Autowired
    HomePageService homePageService;

    @Autowired
    SymposiumService symposiumService;

    @Autowired
    FacebookService fbService;

    @Autowired
    TwitterService twitterService;

    @RequestMapping("/allUsers")
    public HomePageResponse allUsers() {
        return homePageService.fetchAllUsers();
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public ResponseEntity<String> addUser(@RequestBody SymposiumUserDTO user) {

        if (user.isValid()) {
            try {
                symposiumService.persistUser(user);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (UserAlreadyExists ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @RequestMapping(value = "/synchronize/{username}", method = RequestMethod.PUT)
    public ResponseEntity<String> synchronizeUser(@PathVariable String username) {

        //TODO throw some exception here
        SymposiumUserDTO user = symposiumService.findUser(username);
        try {
            twitterService.fetchAndStoreAllUserTweets(user);
            fbService.fetchAndStoreAllUserPosts(user);
            symposiumService.retrieveAndSaveSymposiumUser();
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (SocialMediaInformationNotRetrieved ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

}
