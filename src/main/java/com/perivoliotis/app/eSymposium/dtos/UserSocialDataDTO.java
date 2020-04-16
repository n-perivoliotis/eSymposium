package com.perivoliotis.app.eSymposium.dtos;

import com.perivoliotis.app.eSymposium.dtos.facebook.FacebookPostDTO;
import com.perivoliotis.app.eSymposium.dtos.facebook.FacebookUserDTO;
import com.perivoliotis.app.eSymposium.dtos.twitter.TweetDTO;
import com.perivoliotis.app.eSymposium.dtos.twitter.TwitterUserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class UserSocialDataDTO {

    private String symposiumUsername;

    private TwitterUserDTO twitterUser;

    private FacebookUserDTO facebookUser;

    private Set<TweetDTO> tweets;

    private Set<FacebookPostDTO> fbPosts;
}
