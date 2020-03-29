package com.perivoliotis.app.eSymposium.entities.twitter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@ToString
@Document
public class UserTweets {

    @Id
    private long id;

    private TwitterUser aUser;

    private Set<Tweet> tweets;

}
