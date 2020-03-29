package com.perivoliotis.app.eSymposium.repos.custom;

import com.mongodb.WriteResult;
import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class UserPostsRepositoryImpl implements UserPostsRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public boolean saveOrUpdate(UserPosts userPosts) {

        Query query = new Query(Criteria.where("fbUser.username").is(userPosts.getFbUser().getUsername()));
        List<UserPosts> foundList  = mongoTemplate.find(query, UserPosts.class);

        if (foundList.isEmpty()){
            mongoTemplate.save(userPosts);
        } else if (foundList.size() == 1){
            foundList.get(0).getFacebookPosts().addAll(userPosts.getFacebookPosts());
            Update update = new Update();
            update.set("facebookPosts", foundList.get(0).getFacebookPosts());
            WriteResult result = mongoTemplate.updateFirst(query, update, UserTweets.class);
            return result != null;
        } else {
            return false;
        }
        return true;
    }
}
