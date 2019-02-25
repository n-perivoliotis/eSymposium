package com.perivoliotis.app.eSymposium.core.DAOs;


import com.mongodb.MongoClient;
import com.perivoliotis.app.eSymposium.core.entities.TwitterUser;
import com.perivoliotis.app.eSymposium.core.entities.UserTweets;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.ArrayList;
import java.util.List;

public class UserTweetsDAO extends BasicDAO<UserTweets, String> {

    public UserTweetsDAO(Morphia morphia, MongoClient mongo, String dbName) {
        super(mongo, morphia, dbName);
    }

    public boolean saveOrUpdate(UserTweets userTweets) {

        Query<UserTweets> query = getDatastore().createQuery(UserTweets.class);
        query.criteria("aUser.username").equal(userTweets.getaUser().getUsername());
        QueryResults<UserTweets> foundEntities = find(query);

        List<UserTweets> foundList = foundEntities.asList();
        if (foundList.isEmpty()){
            save(userTweets);
        } else if (foundList.size() == 1){
            foundList.get(0).getTweets().addAll(userTweets.getTweets());
            UpdateOperations<UserTweets> updateOp = getDatastore().createUpdateOperations(UserTweets.class);
            updateOp.set("tweets", foundList.get(0).getTweets());
            update(query, updateOp);
        }else {
            return false;
        }
        return true;
    }

    public List<TwitterUser> findAllTwitterUsers() {

        Query<UserTweets> query = getDatastore().createQuery(UserTweets.class);
        query.project("aUser", true);
        List<UserTweets> userTweets = find(query).asList();

        List<TwitterUser> users = new ArrayList<>();

        for (UserTweets userTweet : userTweets) {
            users.add(userTweet.getaUser());
        }

        return users;
    }
}
