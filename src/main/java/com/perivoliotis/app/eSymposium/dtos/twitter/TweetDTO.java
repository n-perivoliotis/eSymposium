package com.perivoliotis.app.eSymposium.dtos.twitter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class TweetDTO {

    private String text;

    private int retweets;

    private int likes;

    private LocalDateTime timestamp;

    private String geolocation;

    private List<String> hashtags;

    private List<String> mediaUrls;
}
