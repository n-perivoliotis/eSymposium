package com.perivoliotis.app.eSymposium.services;

import com.perivoliotis.app.eSymposium.dtos.SymposiumUserDTO;
import com.perivoliotis.app.eSymposium.entities.facebook.FacebookUser;
import com.perivoliotis.app.eSymposium.entities.facebook.UserPosts;
import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import com.perivoliotis.app.eSymposium.entities.twitter.TwitterUser;
import com.perivoliotis.app.eSymposium.entities.twitter.UserTweets;
import com.perivoliotis.app.eSymposium.exceptions.FacebookScrapperError;
import com.perivoliotis.app.eSymposium.exceptions.InvalidDatabaseState;
import com.perivoliotis.app.eSymposium.exceptions.SocialMediaInformationNotRetrieved;
import com.perivoliotis.app.eSymposium.exceptions.UserAlreadyExists;
import com.perivoliotis.app.eSymposium.integration.clients.FacebookClient;
import com.perivoliotis.app.eSymposium.integration.clients.TwitterClient;
import com.perivoliotis.app.eSymposium.repos.SymposiumUserRepository;
import com.perivoliotis.app.eSymposium.repos.UserPostsRepository;
import com.perivoliotis.app.eSymposium.repos.UserTweetsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserManagementServiceTest {

    @Autowired
    UserManagementService userManagementService;

    @MockBean
    SymposiumUserRepository symposiumUserRepository;

    @MockBean
    UserTweetsRepository userTweetsRepository;

    @MockBean
    UserPostsRepository userPostsRepository;

    @MockBean
    FacebookClient facebookClient;

    @MockBean
    TwitterClient twitterClient;

    @Before
    public void setUp() {

    }

    @Test
    public void when_request_all_users_and_exist_many_all_should_return() {

        //mock data
        List<SymposiumUser> allUsers = createMockSymposiumUsersTwo();

        Mockito.when(symposiumUserRepository.findAll())
                .thenReturn(allUsers);

        // call service
        List<SymposiumUserDTO> dtos = userManagementService.fetchAllUsers();

        // assertions
        assertThat(dtos.size(), is(2));
        assertThat(dtos.get(0).getSymposiumUsername(), is("aleksis"));
        assertThat(dtos.get(0).getFbUsername(), is("aleksis_fb"));
        assertThat(dtos.get(0).getTwitterUsername(), is("aleksis_tw"));
        assertThat(dtos.get(1).getSymposiumUsername(), is("kuriakos"));
        assertThat(dtos.get(1).getFbUsername(), is("kuriakos_fb"));
        assertThat(dtos.get(1).getTwitterUsername(), is("kuriakos_tw"));
    }

    @Test
    public void when_request_all_users_and_exist_none_empty_list_should_return() {

        //mock data

        Mockito.when(symposiumUserRepository.findAll())
                .thenReturn(new ArrayList<>());

        // call service
        List<SymposiumUserDTO> dtos = userManagementService.fetchAllUsers();

        // assertions
        assertThat(dtos.size(), is(0));
    }

    @Test
    public void when_ask_to_save_a_new_user_should_be_saved() throws UserAlreadyExists {

        //mock data
        SymposiumUserDTO s1 = new SymposiumUserDTO();
        s1.setSymposiumUsername("aleksis");
        s1.setFbUsername("aleksis_fb");
        s1.setTwitterUsername("aleksis_tw");

        Mockito.when(symposiumUserRepository.save(s1.asEntity()))
                .thenReturn(any());

        // call service
        userManagementService.persistUser(s1);

        // assertions
        verify(symposiumUserRepository, times(1)).save(any(SymposiumUser.class));
    }

    @Test(expected = UserAlreadyExists.class)
    public void when_ask_to_save_an_existing_user_should_throw_exception() throws UserAlreadyExists {

        //mock data
        SymposiumUserDTO s1 = new SymposiumUserDTO();
        s1.setSymposiumUsername("aleksis");
        s1.setFbUsername("aleksis_fb");
        s1.setTwitterUsername("aleksis_tw");

        doThrow(DuplicateKeyException.class).when(symposiumUserRepository).save(any(SymposiumUser.class));

        // call service
        userManagementService.persistUser(s1);
    }

    @Test
    public void when_username_is_valid_user_should_be_retrieved() {

        //mock data
        Mockito.when(symposiumUserRepository.findBySymposiumUsername(anyString()))
                .thenReturn(createMockSymposiumUserOne());

        // call service
        SymposiumUserDTO result = userManagementService.findSymposiumUser(anyString());

        // assertions

        assertNotNull(result);
    }

    @Test(expected = InvalidDatabaseState.class)
    public void when_username_does_not_exist_should_throw_exception() {

        //mock data
        Mockito.when(symposiumUserRepository.findBySymposiumUsername(anyString()))
                .thenReturn(new ArrayList<>());

        // call service
        userManagementService.findSymposiumUser(anyString());
    }

    @Test(expected = InvalidDatabaseState.class)
    public void when_username_has_duplicate_entries_should_throw_exception() {

        //mock data
        Mockito.when(symposiumUserRepository.findBySymposiumUsername(anyString()))
                .thenReturn(createMockSymposiumUsersTwo());

        // call service
        userManagementService.findSymposiumUser(anyString());
    }

    @Test
    public void when_tweets_and_posts_retrieved_should_be_saved() throws Exception {

        //mock data
        Mockito.when(twitterClient.getAllTweetsFromUser(anyString()))
                .thenReturn(new UserTweets());

        Mockito.when(facebookClient.getAllFbPostsFromUser(anyString()))
                .thenReturn(new UserPosts());

        Mockito.when(symposiumUserRepository.findBySymposiumUsername(anyString()))
                .thenReturn(createMockSymposiumUserOne());

        doNothing().when(userTweetsRepository).saveOrUpdate(any(UserTweets.class));
        doNothing().when(userPostsRepository).saveOrUpdate(any(UserPosts.class));

        // call service
        userManagementService.synchronizeUser(anyString());

        // assertions
        verify(twitterClient, times(1)).getAllTweetsFromUser(anyString());
        verify(facebookClient, times(1)).getAllFbPostsFromUser(anyString());
        verify(userTweetsRepository, times(1)).saveOrUpdate(any(UserTweets.class));
        verify(userPostsRepository, times(1)).saveOrUpdate(any(UserPosts.class));
    }

    @Test(expected = SocialMediaInformationNotRetrieved.class)
    public void when_tweets_throw_exception_should_throw_exception() throws Exception {

        //mock data
        doThrow(TwitterException.class).when(twitterClient).getAllTweetsFromUser(anyString());

        Mockito.when(facebookClient.getAllFbPostsFromUser(anyString()))
                .thenReturn(new UserPosts());

        Mockito.when(symposiumUserRepository.findBySymposiumUsername(anyString()))
                .thenReturn(createMockSymposiumUserOne());

        doNothing().when(userTweetsRepository).saveOrUpdate(any(UserTweets.class));
        doNothing().when(userPostsRepository).saveOrUpdate(any(UserPosts.class));

        // call service
        userManagementService.synchronizeUser(anyString());
    }

    @Test(expected = SocialMediaInformationNotRetrieved.class)
    public void when_posts_throw_exception_should_throw_exception() throws Exception {

        //mock data
        doThrow(FacebookScrapperError.class).when(facebookClient).getAllFbPostsFromUser(anyString());

        Mockito.when(twitterClient.getAllTweetsFromUser(anyString()))
                .thenReturn(new UserTweets());

        Mockito.when(symposiumUserRepository.findBySymposiumUsername(anyString()))
                .thenReturn(createMockSymposiumUserOne());

        doNothing().when(userTweetsRepository).saveOrUpdate(any(UserTweets.class));
        doNothing().when(userPostsRepository).saveOrUpdate(any(UserPosts.class));

        // call service
        userManagementService.synchronizeUser(anyString());
    }

    private List<SymposiumUser> createMockSymposiumUsersTwo() {

        List<SymposiumUser> allUsers = new ArrayList<>();

        SymposiumUser s1 = new SymposiumUser();
        s1.setSymposiumUsername("aleksis");
        TwitterUser tu1 = new TwitterUser();
        tu1.setUsername("aleksis_tw");
        FacebookUser fu1 = new FacebookUser();
        fu1.setUsername("aleksis_fb");
        s1.setTwitterUser(tu1);
        s1.setFacebookUser(fu1);

        SymposiumUser s2 = new SymposiumUser();
        s2.setSymposiumUsername("kuriakos");
        TwitterUser tu2 = new TwitterUser();
        tu2.setUsername("kuriakos_tw");
        FacebookUser fu2 = new FacebookUser();
        fu2.setUsername("kuriakos_fb");
        s2.setTwitterUser(tu2);
        s2.setFacebookUser(fu2);

        allUsers.add(s1);
        allUsers.add(s2);

        return allUsers;
    }

    private List<SymposiumUser> createMockSymposiumUserOne() {

        List<SymposiumUser> allUsers = new ArrayList<>();

        SymposiumUser s1 = new SymposiumUser();
        s1.setSymposiumUsername("aleksis");
        TwitterUser tu1 = new TwitterUser();
        tu1.setUsername("aleksis_tw");
        FacebookUser fu1 = new FacebookUser();
        fu1.setUsername("aleksis_fb");
        s1.setTwitterUser(tu1);
        s1.setFacebookUser(fu1);

        allUsers.add(s1);

        return allUsers;
    }

}
