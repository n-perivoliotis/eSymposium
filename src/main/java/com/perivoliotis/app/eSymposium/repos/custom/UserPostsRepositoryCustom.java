package com.perivoliotis.app.eSymposium.repos.custom;

import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;

public interface UserPostsRepositoryCustom {

    boolean saveOrUpdate(UserPosts userPosts);
}
