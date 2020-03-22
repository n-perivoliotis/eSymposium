package com.perivoliotis.app.eSymposium.entities;

import org.mongodb.morphia.annotations.Embedded;

import java.util.Date;

@Embedded
public class TwitterUser {

    private String username;

    private String profileName;

    private String description;

    private String location;

    private Date dateAccountCreated;

    private String profilePictureUrl;

    private String backgroundPictureUrl;

    private Integer numOfTweets;

    private Integer follows;

    private Integer followers;

    private Integer likes;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateAccountCreated() {
        return dateAccountCreated;
    }

    public void setDateAccountCreated(Date dateAccountCreated) {
        this.dateAccountCreated = dateAccountCreated;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getBackgroundPictureUrl() {
        return backgroundPictureUrl;
    }

    public void setBackgroundPictureUrl(String backgroundPictureUrl) {
        this.backgroundPictureUrl = backgroundPictureUrl;
    }

    public Integer getNumOfTweets() {
        return numOfTweets;
    }

    public void setNumOfTweets(Integer numOfTweets) {
        this.numOfTweets = numOfTweets;
    }

    public Integer getFollows() {
        return follows;
    }

    public void setFollows(Integer follows) {
        this.follows = follows;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
