package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.entities.facebook.FacebookPost;
import com.perivoliotis.app.eSymposium.entities.facebook.FacebookUser;
import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.integration.clients.FacebookClient;
import com.perivoliotis.app.eSymposium.repos.UserPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class FacebookService {

    @Autowired
    UserPostsRepository userPostsRepository;

    @Autowired
    FacebookClient facebookClient;

    public void fetchAndStoreAllUserPosts(FacebookUser aUser) throws Exception {

        UserPosts usersPosts = facebookClient.getAllFbPostsFromUser(aUser.getUsername().replace("@",""));

        System.out.println("Showing fb home timeline.");
        for (FacebookPost post : usersPosts.getFacebookPosts()) {
            System.out.println(post.getDescriptionText());
        }

        if (userPostsRepository.saveOrUpdate(usersPosts)) {
            System.out.println("Database successfully updated");
        } else {
            System.out.println("Some error occurred while trying to update database");
        }
    }

}
