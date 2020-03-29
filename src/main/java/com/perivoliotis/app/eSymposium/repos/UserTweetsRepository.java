package com.perivoliotis.app.eSymposium.repos;

import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import com.perivoliotis.app.eSymposium.repos.custom.UserTweetsRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTweetsRepository extends MongoRepository<UserTweets, Long>, UserTweetsRepositoryCustom {

    @Query("{'aUser.username': ?0}")
    List<UserTweets> findByUsername(final String username);

}
