package com.perivoliotis.app.eSymposium.core.entities;

import org.mongodb.morphia.annotations.Entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class FacebookPost {


    private String descriptionText;

    private String linkToMoreDescription;

    private int reactions;

    private int totalComments;

    private LocalDateTime timestamp;

    private List<String> videoUrls;

    private List<String> imagesUrls;

    private List<String> onlineUrls;

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getLinkToMoreDescription() {
        return linkToMoreDescription;
    }

    public void setLinkToMoreDescription(String linkToMoreDescription) {
        this.linkToMoreDescription = linkToMoreDescription;
    }

    public int getReactions() {
        return reactions;
    }

    public void setReactions(int reactions) {
        this.reactions = reactions;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(List<String> videoUrls) {
        this.videoUrls = videoUrls;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public List<String> getOnlineUrls() {
        return onlineUrls;
    }

    public void setOnlineUrls(List<String> onlineUrls) {
        this.onlineUrls = onlineUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FacebookPost that = (FacebookPost) o;
        return Objects.equals(descriptionText, that.descriptionText) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptionText, timestamp);
    }
}
