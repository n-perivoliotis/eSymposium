package com.perivoliotis.app.eSymposium.entities.symposium;

import com.perivoliotis.app.eSymposium.entities.facebook.FacebookUser;
import com.perivoliotis.app.eSymposium.entities.twitter.TwitterUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document
public class SymposiumUser {

    @Id
    private long id;

    private String symposiumUsername;

    private TwitterUser twitterUser;

    private FacebookUser facebookUser;

}
