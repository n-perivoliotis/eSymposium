package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.responses.HomePageResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HomePageService {

    public HomePageResponse fetchAllUsers() {
        HomePageResponse response = new HomePageResponse();
        List<String> users = new ArrayList<>();
        users.add("Alexis Tsipras");
        users.add("Kyriakos Mitsotakis");
        response.seteSymposiumUsernames(users);
        return response;
    }
}
