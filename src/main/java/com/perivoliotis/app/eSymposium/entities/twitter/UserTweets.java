package com.perivoliotis.app.eSymposium.entities.twitter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.Set;

@Getter
@Setter
@ToString
@Document(collection = "UserTweets")
public class UserTweets {

    @Id
    private BigInteger id;

    private TwitterUser aUser;

    private Set<Tweet> tweets;

}
