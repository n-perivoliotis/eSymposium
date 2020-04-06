import com.perivoliotis.app.eSymposium.entities.facebook.FacebookUser;
import com.perivoliotis.app.eSymposium.entities.twitter.TwitterUser;
import com.perivoliotis.app.eSymposium.services.FacebookService;
import com.perivoliotis.app.eSymposium.services.SymposiumService;
import com.perivoliotis.app.eSymposium.services.TwitterService;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception{


       FacebookService fbService = new FacebookService();
       List<FacebookUser> fbUsers = new ArrayList<>();

       createFbUser("@tsiprasalexis", fbUsers);
       createFbUser("@cnninternational", fbUsers);
       createFbUser("@financialtimes", fbUsers);
       createFbUser("@GreekFreakOfficial", fbUsers);
       createFbUser("helenapaparizouofficial", fbUsers);
       fbService.fetchAndStoreAllUserPosts(fbUsers.get(0));

       TwitterService twitterService = new TwitterService();
       List<TwitterUser> twitterUsers = new ArrayList<>();

       createTwitterUser("atsipras", twitterUsers);
       createTwitterUser("cnni", twitterUsers);
       createTwitterUser("FinancialTimes", twitterUsers);
       createTwitterUser("Giannis_An34", twitterUsers);
       createTwitterUser("paparizouhelena", twitterUsers);
       twitterService.fetchAndStoreAllUserTweets(twitterUsers.get(0));

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
