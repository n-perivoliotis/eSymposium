package com.perivoliotis.app.eSymposium.entities;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;

import java.util.Set;

@Entity
public class UserPosts extends BaseEntity {

    @Embedded()
    private FacebookUser fbUser;

    private Set<FacebookPost> facebookPosts;

    public FacebookUser getFbUser() {
        return fbUser;
    }

    public void setFbUser(FacebookUser fbUser) {
        this.fbUser = fbUser;
    }

    public Set<FacebookPost> getFacebookPosts() {
        return facebookPosts;
    }

    public void setFacebookPosts(Set<FacebookPost> facebookPosts) {
        this.facebookPosts = facebookPosts;
    }
}
