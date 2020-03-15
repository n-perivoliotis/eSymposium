package com.perivoliotis.app.eSymposium.core.services;

import com.mongodb.MongoClient;
import com.perivoliotis.app.eSymposium.core.DAOs.UserPostsDAO;
import com.perivoliotis.app.eSymposium.core.entities.FacebookPost;
import com.perivoliotis.app.eSymposium.core.entities.FacebookUser;
import com.perivoliotis.app.eSymposium.core.entities.UserPosts;
import com.perivoliotis.app.eSymposium.core.utilities.FacebookUtils;
import org.mongodb.morphia.Morphia;

import java.util.List;

public class FacebookService {


    public void fetchAndStoreAllUsersPosts(List<FacebookUser> users) throws Exception{

        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        UserPostsDAO userPostsDAO = new UserPostsDAO(new Morphia(), mongo, "eSymposiumDb");

        for (FacebookUser aUser : users){

            UserPosts usersPosts = FacebookUtils.getAllFbPostsFromUser(aUser.getUsername().replace("@",""));

            System.out.println("Showing fb home timeline.");
            for (FacebookPost post : usersPosts.getFacebookPosts()) {
                System.out.println(post.getDescriptionText());
            }

            if(userPostsDAO.saveOrUpdate(usersPosts)){
                System.out.println("Database successfully updated");
            }else{
                System.out.println("Some error occurred while trying to update database");
            }
        }
    }






}
