package com.perivoliotis.app.eSymposium.entities;

import org.mongodb.morphia.annotations.Embedded;

import java.time.LocalDateTime;

@Embedded
public class FacebookUser {

    private String pageName;

    private String username;

    private String description;

    private LocalDateTime birthday;

    private String profilePictureUrl;

    private String backgroundPictureUrl;

    private String placeOfOrigin;

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
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

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
    }
}
