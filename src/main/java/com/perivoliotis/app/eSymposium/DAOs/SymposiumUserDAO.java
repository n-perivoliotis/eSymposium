package com.perivoliotis.app.eSymposium.DAOs;

import com.mongodb.MongoClient;
import com.perivoliotis.app.eSymposium.entities.SymposiumUser;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.List;

public class SymposiumUserDAO extends BasicDAO<SymposiumUser, String> {

    public SymposiumUserDAO(Morphia morphia, MongoClient mongo, String dbName) {
        super(mongo, morphia, dbName);
    }

    public boolean saveOrUpdate(SymposiumUser symposiumUser){

        Query<SymposiumUser> query = getDatastore().createQuery(SymposiumUser.class);
        query.criteria("symposiumUsername").equal(symposiumUser.getSymposiumUsername());
        QueryResults<SymposiumUser> foundEntities = find(query);

        List<SymposiumUser> foundList = foundEntities.asList();
        if (foundList.isEmpty()){
            save(symposiumUser);
        } else if (foundList.size() == 1){

            UpdateOperations<SymposiumUser> updateOp = getDatastore().createUpdateOperations(SymposiumUser.class);
            updateOp.set("facebookUser", symposiumUser.getFacebookUser());
            updateOp.set("twitterUser", symposiumUser.getTwitterUser());
            update(query, updateOp);
        }else {
            return false;
        }
        return true;
    }
}
