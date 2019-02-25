package com.perivoliotis.app.eSymposium.core.entities;

import org.mongodb.morphia.annotations.Entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Tweet {

    private String text;

    private int retweets;

    private int likes;

    private LocalDateTime timestamp;

    private String geolocation;

    private List<String> hashtags;

    // maps both urlEntities and mediaEntities
    private List<String> mediaUrls;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRetweets() {
        return retweets;
    }

    public void setRetweets(int retweets) {
        this.retweets = retweets;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return Objects.equals(text, tweet.text) &&
                Objects.equals(timestamp, tweet.timestamp) &&
                Objects.equals(geolocation, tweet.geolocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, timestamp, geolocation);
    }
}
