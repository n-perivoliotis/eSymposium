package com.perivoliotis.app.eSymposium.controllers;

import com.perivoliotis.app.eSymposium.responses.HomePageResponse;
import com.perivoliotis.app.eSymposium.services.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SymposiumController {

    @Autowired
    HomePageService homePageService;

    @RequestMapping("/")
    public HomePageResponse index() {
        return homePageService.fetchAllUsers();
    }

}
