package com.perivoliotis.app.eSymposium.repos;

import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.repos.custom.UserPostsRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostsRepository extends MongoRepository<UserPosts, Long>, UserPostsRepositoryCustom {

}
