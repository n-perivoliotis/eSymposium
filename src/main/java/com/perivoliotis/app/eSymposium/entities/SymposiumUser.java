package com.perivoliotis.app.eSymposium.entities;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;

@Entity
public class SymposiumUser extends BaseEntity {

    private String symposiumUsername;

    @Embedded
    private TwitterUser twitterUser;

    @Embedded
    private FacebookUser facebookUser;

    public String getSymposiumUsername() {
        return symposiumUsername;
    }

    public void setSymposiumUsername(String symposiumUsername) {
        this.symposiumUsername = symposiumUsername;
    }

    public TwitterUser getTwitterUser() {
        return twitterUser;
    }

    public void setTwitterUser(TwitterUser twitterUser) {
        this.twitterUser = twitterUser;
    }

    public FacebookUser getFacebookUser() {
        return facebookUser;
    }

    public void setFacebookUser(FacebookUser facebookUser) {
        this.facebookUser = facebookUser;
    }
}
