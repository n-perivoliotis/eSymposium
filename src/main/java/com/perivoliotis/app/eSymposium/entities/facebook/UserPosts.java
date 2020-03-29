package com.perivoliotis.app.eSymposium.entities.facebook;

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
public class UserPosts {

    @Id
    private long id;

    private FacebookUser fbUser;

    private Set<FacebookPost> facebookPosts;
}
