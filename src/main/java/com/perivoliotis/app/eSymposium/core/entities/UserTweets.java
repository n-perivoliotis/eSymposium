package com.perivoliotis.app.eSymposium.core.entities;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;

import java.util.Set;

@Entity
public class UserTweets extends BaseEntity {

    @Embedded
    private TwitterUser aUser;

    private Set<Tweet> tweets;

    public TwitterUser getaUser() {
        return aUser;
    }

    public void setaUser(TwitterUser aUser) {
        this.aUser = aUser;
    }

    public Set<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(Set<Tweet> tweets) {
        this.tweets = tweets;
    }

}
