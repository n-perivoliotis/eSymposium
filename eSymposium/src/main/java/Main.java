import com.perivoliotis.app.eSymposium.core.entities.FacebookUser;
import com.perivoliotis.app.eSymposium.core.entities.TwitterUser;
import com.perivoliotis.app.eSymposium.core.services.FacebookService;
import com.perivoliotis.app.eSymposium.core.services.SymposiumService;
import com.perivoliotis.app.eSymposium.core.services.TwitterService;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception{


//        FacebookService fbService = new FacebookService();
//        List<FacebookUser> fbUsers = new ArrayList<>();
//
//        createFbUser("@tsiprasalexis", fbUsers);
//
//        createFbUser("@cnninternational", fbUsers);
//
//        fbService.fetchAndStoreAllUsersPosts(fbUsers);


        TwitterService twitterService = new TwitterService();
        List<TwitterUser> twitterUsers = new ArrayList<>();

        createTwitterUser("atsipras", twitterUsers);

        createTwitterUser("cnni", twitterUsers);

        twitterService.fetchAndStoreAllUsersTweets(twitterUsers);

        SymposiumService symposiumService = new SymposiumService();
        symposiumService.retrieveAndSaveSymposiumUser();

    }

    private static void createTwitterUser(String username, List<TwitterUser> users){

        TwitterUser aUser = new TwitterUser();
        aUser.setUsername(username);

        users.add(aUser);
    }

    private static void createFbUser(String username, List<FacebookUser> fbUsers){

        FacebookUser fbUser = new FacebookUser();
        fbUser.setUsername(username);

        fbUsers.add(fbUser);
    }

}
