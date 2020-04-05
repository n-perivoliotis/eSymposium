package com.perivoliotis.app.eSymposium.integration.clients;

import com.perivoliotis.app.eSymposium.entities.twitter.Tweet;
import com.perivoliotis.app.eSymposium.entities.twitter.TwitterUser;
import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.time.ZoneId;
import java.util.*;

@Component
public class TwitterClient {

    // The factory instance is re-useable and thread safe.
    private final Twitter twitter = TwitterFactory.getSingleton();

    public UserTweets getAllTweetsFromUser(String username) throws TwitterException {

        int pageno = 1;

        Set<Tweet> allTweets = new HashSet<>();
        UserTweets userTweets = new UserTweets();
        int initialSize;
        int finalSize;

        do {
            initialSize = allTweets.size();
            Paging page = new Paging(pageno++ , 200);
            List<Status> statuses = twitter.getUserTimeline(username, page);

            handleRateLimit(((ResponseList<Status>) statuses).getRateLimitStatus());

            if (initialSize == 0 && !statuses.isEmpty() && statuses.get(0).getUser() != null){
                userTweets.setAUser(toUser(statuses.get(0).getUser()));
            }

            allTweets.addAll(convertToTweet(statuses));
            finalSize = allTweets.size();
        } while (finalSize != initialSize);



        userTweets.setTweets(allTweets);

        return userTweets;
    }

    private Set<Tweet> convertToTweet(List<Status> statuses){

        Set<Tweet> converted = new HashSet<>();

        for(Status status : statuses){
            if (status != null){
                converted.add(toTweet(status));
            }
        }

        return converted;
    }

    private Tweet toTweet(Status status){

        Tweet tweet = new Tweet();

        tweet.setText(status.getText());
        tweet.setTimestamp(status.getCreatedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        tweet.setRetweets(status.getRetweetCount());
        tweet.setLikes(status.getFavoriteCount());
        tweet.setGeolocation(status.getGeoLocation() != null ? status.getGeoLocation().toString() : null);

        List<String> hashtags = new ArrayList<>();

        for (HashtagEntity hash : status.getHashtagEntities()){
            hashtags.add(hash.getText());
        }

        tweet.setHashtags(hashtags);

        List<String> mediaUrls = new ArrayList<>();

        for (MediaEntity media : status.getMediaEntities()){
            mediaUrls.add(media.getMediaURLHttps());
        }

        for (URLEntity url : status.getURLEntities()){
            mediaUrls.add(url.getExpandedURL());
        }

        tweet.setMediaUrls(mediaUrls);

        return tweet;
    }

    private TwitterUser toUser(User user){

        TwitterUser twitterUser = new TwitterUser();

        twitterUser.setUsername(user.getScreenName());
        twitterUser.setProfileName(user.getName());
        twitterUser.setDescription(user.getDescription());
        twitterUser.setFollowers(user.getFollowersCount());
        twitterUser.setFollows(user.getFriendsCount());
        twitterUser.setNumOfTweets(user.getStatusesCount());
        twitterUser.setDateAccountCreated(user.getCreatedAt());
        twitterUser.setBackgroundPictureUrl(user.getProfileBannerURL());
        twitterUser.setProfilePictureUrl(user.getProfileImageURL());
        twitterUser.setLikes(user.getFavouritesCount());
        twitterUser.setLocation(user.getLocation());

        return twitterUser;
    }


    private void handleRateLimit(RateLimitStatus rateLimitStatus) {
        int remaining = rateLimitStatus.getRemaining();
        if (remaining == 0) {
            int resetTime = rateLimitStatus.getSecondsUntilReset() + 5;
            int sleep = (resetTime * 1000);
            try {
                Thread.sleep(sleep > 0 ? sleep : 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
