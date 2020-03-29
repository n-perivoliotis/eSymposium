package com.perivoliotis.app.eSymposium.entities.twitter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
public class Tweet {

    private String text;

    private int retweets;

    private int likes;

    private LocalDateTime timestamp;

    private String geolocation;

    private List<String> hashtags;

    // maps both urlEntities and mediaEntities
    private List<String> mediaUrls;

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
