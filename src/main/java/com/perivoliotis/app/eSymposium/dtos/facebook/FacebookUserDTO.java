package com.perivoliotis.app.eSymposium.dtos.facebook;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class FacebookUserDTO {

    private String pageName;

    private String username;

    private String description;

    private LocalDateTime birthday;

    private String profilePictureUrl;

    private String backgroundPictureUrl;

    private String placeOfOrigin;

}
