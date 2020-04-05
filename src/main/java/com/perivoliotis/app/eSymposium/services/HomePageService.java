package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import com.perivoliotis.app.eSymposium.repos.SymposiumUserRepository;
import com.perivoliotis.app.eSymposium.responses.HomePageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HomePageService {

    @Autowired
    SymposiumUserRepository symposiumUserRepository;

    public HomePageResponse fetchAllUsers() {

        HomePageResponse response = new HomePageResponse();
        List<String> users = new ArrayList<>();

        List<SymposiumUser> sUsers = symposiumUserRepository.findAll();

        for (SymposiumUser sUser : sUsers) {
            users.add(sUser.getSymposiumUsername());
        }
        response.seteSymposiumUsernames(users);

        return response;
    }
}
