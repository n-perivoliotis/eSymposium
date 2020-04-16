package com.perivoliotis.app.eSymposium.repos;

import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import com.perivoliotis.app.eSymposium.repos.custom.UserPostsRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPostsRepository extends MongoRepository<UserPosts, Long>, UserPostsRepositoryCustom {

    @Query("{'fbUser.username': ?0}")
    List<UserPosts> findByUsername(final String username);

}
