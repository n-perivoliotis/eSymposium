package com.perivoliotis.app.eSymposium.dtos.twitter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class TwitterUserDTO {

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
}
