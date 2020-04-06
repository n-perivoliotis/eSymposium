package com.perivoliotis.app.eSymposium.controllers;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.exceptions.UserAlreadyExists;
import com.perivoliotis.app.eSymposium.responses.HomePageResponse;
import com.perivoliotis.app.eSymposium.services.HomePageService;
import com.perivoliotis.app.eSymposium.services.SymposiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SymposiumController {

    @Autowired
    HomePageService homePageService;

    @Autowired
    SymposiumService symposiumService;

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

}
