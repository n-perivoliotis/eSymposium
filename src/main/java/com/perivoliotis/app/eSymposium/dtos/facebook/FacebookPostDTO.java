package com.perivoliotis.app.eSymposium.dtos.facebook;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class FacebookPostDTO {

    private String descriptionText;

    private int reactions;

    private int totalComments;

    private LocalDateTime timestamp;

    private List<String> videoUrls;

    private List<String> imagesUrls;

    private List<String> onlineUrls;
}
