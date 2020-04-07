package com.perivoliotis.app.eSymposium.repos.custom;

import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import com.perivoliotis.app.eSymposium.exceptions.DatabaseOperationFailed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class UserTweetsRepositoryImpl implements UserTweetsRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void saveOrUpdate(UserTweets userTweets) {

        Query query = new Query(Criteria.where("aUser.username").is(userTweets.getAUser().getUsername()));
        List<UserTweets> foundList  = mongoTemplate.find(query, UserTweets.class);

        if (foundList.isEmpty()){
            mongoTemplate.save(userTweets);
        } else if (foundList.size() == 1){
            foundList.get(0).getTweets().addAll(userTweets.getTweets());
            Update update = new Update();
            update.set("tweets", foundList.get(0).getTweets());
            mongoTemplate.updateFirst(query, update, UserTweets.class);
        } else {
            throw new DatabaseOperationFailed(
                    String.format("Duplicate entries found in database for twitter user %s", userTweets.getAUser().getUsername())
            );
        }
    }

}
