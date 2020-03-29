package com.perivoliotis.app.eSymposium.repos.custom;

import com.mongodb.WriteResult;
import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class SymposiumUserRepositoryImpl implements SymposiumUserRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public boolean saveOrUpdate(SymposiumUser symposiumUser) {

        Query query = new Query(Criteria.where("symposiumUsername").is(symposiumUser.getSymposiumUsername()));
        List<SymposiumUser> foundList  = mongoTemplate.find(query, SymposiumUser.class);

        if (foundList.isEmpty()){
            mongoTemplate.save(symposiumUser);
        } else if (foundList.size() == 1){
            Update update = new Update();
            update.set("facebookUser", symposiumUser.getFacebookUser());
            update.set("twitterUser", symposiumUser.getTwitterUser());
            WriteResult result = mongoTemplate.updateFirst(query, update, SymposiumUser.class);
            return result != null;
        } else {
            return false;
        }
        return true;
    }

}
