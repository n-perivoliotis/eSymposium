package com.perivoliotis.app.eSymposium.controllers;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.exceptions.SocialMediaInformationNotRetrieved;
import com.perivoliotis.app.eSymposium.exceptions.UserAlreadyExists;
import com.perivoliotis.app.eSymposium.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SymposiumController {

    @Autowired
    UserManagementService userManagementService;

    @RequestMapping(value = "/allUsers", method = RequestMethod.GET)
    public List<SymposiumUserDTO> allUsers() {
        return userManagementService.fetchAllUsers();
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
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

    @RequestMapping(value = "/synchronize/{username}", method = RequestMethod.PUT)
    public ResponseEntity<String> synchronizeUser(@PathVariable String username) {

        try {
            userManagementService.synchronizeUser(username);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (SocialMediaInformationNotRetrieved ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

}
