package com.perivoliotis.app.eSymposium.entities.symposium;

import com.perivoliotis.app.eSymposium.entities.facebook.FacebookUser;
import com.perivoliotis.app.eSymposium.entities.twitter.TwitterUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Getter
@Setter
@ToString
@Document(collection = "SymposiumUser")
public class SymposiumUser {

    @Id
    private BigInteger _id;

    @Indexed(unique = true)
    private String symposiumUsername;

    private TwitterUser twitterUser;

    private FacebookUser facebookUser;

}
