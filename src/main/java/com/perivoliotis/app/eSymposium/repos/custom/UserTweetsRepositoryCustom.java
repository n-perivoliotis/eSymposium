package com.perivoliotis.app.eSymposium.repos.custom;

import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;

public interface UserTweetsRepositoryCustom {

    boolean saveOrUpdate(UserTweets userTweets);
}
