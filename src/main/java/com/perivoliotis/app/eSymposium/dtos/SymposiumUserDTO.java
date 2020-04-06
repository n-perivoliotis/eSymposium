package com.perivoliotis.app.eSymposium.dtos;

import com.perivoliotis.app.eSymposium.entities.facebook.FacebookUser;
import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import com.perivoliotis.app.eSymposium.entities.twitter.TwitterUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ToString
public class SymposiumUserDTO {

    private String symposiumUsername;

    private String fbUsername;

    private String twitterUsername;

    public boolean isValid() {

        return !StringUtils.isEmpty(this.fbUsername)
                && !StringUtils.isEmpty(this.twitterUsername)
                && !StringUtils.isEmpty(this.symposiumUsername);
    }

    public SymposiumUser asEntity() {

        SymposiumUser su = new SymposiumUser();

        TwitterUser tu = new TwitterUser();
        tu.setUsername(this.twitterUsername);

        FacebookUser fu = new FacebookUser();
        fu.setUsername(this.fbUsername);

        su.setSymposiumUsername(this.symposiumUsername);
        su.setFacebookUser(fu);
        su.setTwitterUser(tu);

        return su;
    }
}
