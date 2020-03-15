package com.perivoliotis.app.eSymposium.core.DAOs;

import com.mongodb.MongoClient;
import com.perivoliotis.app.eSymposium.core.entities.FacebookUser;
import com.perivoliotis.app.eSymposium.core.entities.UserPosts;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.ArrayList;
import java.util.List;

public class UserPostsDAO extends BasicDAO<UserPosts, String> {

    public UserPostsDAO(Morphia morphia, MongoClient mongo, String dbName) {
        super(mongo, morphia, dbName);
    }

    public boolean saveOrUpdate(UserPosts userPosts){

        Query<UserPosts> query = getDatastore().createQuery(UserPosts.class);
        query.criteria("fbUser.username").equal(userPosts.getFbUser().getUsername());
        QueryResults<UserPosts> foundEntities = find(query);

        List<UserPosts> foundList = foundEntities.asList();
        if (foundList.isEmpty()){
            save(userPosts);
        } else if (foundList.size() == 1){
            foundList.get(0).getFacebookPosts().addAll(userPosts.getFacebookPosts());
            UpdateOperations<UserPosts> updateOp = getDatastore().createUpdateOperations(UserPosts.class);
            updateOp.set("facebookPosts", foundList.get(0).getFacebookPosts());
            update(query, updateOp);
        }else {
            return false;
        }

        return true;
    }

    public List<FacebookUser> findAllFbUsers() {

        Query<UserPosts> query = getDatastore().createQuery(UserPosts.class);
        query.project("fbUser", true);
        List<UserPosts> userPosts = find(query).asList();

        List<FacebookUser> users = new ArrayList<>();

        for (UserPosts userPost : userPosts) {
            users.add(userPost.getFbUser());
        }

        return users;
    }

}
