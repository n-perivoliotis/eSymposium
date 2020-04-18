package com.perivoliotis.app.eSymposium.controllers;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.dtos.UserSocialDataDTO;
import com.perivoliotis.app.eSymposium.exceptions.FacebookScrapperError;
import com.perivoliotis.app.eSymposium.exceptions.InvalidUserInformation;
import com.perivoliotis.app.eSymposium.exceptions.UserAlreadyExists;
import com.perivoliotis.app.eSymposium.services.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

import java.util.List;

@RestController
public class SymposiumController {

    Logger logger = LoggerFactory.getLogger(SymposiumController.class);

    @Autowired
    UserManagementService userManagementService;

    @GetMapping(value = "/allUsers")
    public List<SymposiumUserDTO> allUsers() {
        return userManagementService.fetchAllUsers();
    }

    @PostMapping(value = "/addUser")
    public ResponseEntity<String> addUser(@RequestBody SymposiumUserDTO user) {

        if (user.isValid()) {
            try {
                userManagementService.persistUser(user);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (UserAlreadyExists ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping(value = "/synchronize/{username}")
    public ResponseEntity<String> synchronizeUser(@PathVariable String username) throws TwitterException, FacebookScrapperError {

        try {
            userManagementService.synchronizeUser(username);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (InvalidUserInformation ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/displayInfo/{username}")
    public UserSocialDataDTO displayUser(@PathVariable String username) {

       return userManagementService.displayUser(username);
    }

}
