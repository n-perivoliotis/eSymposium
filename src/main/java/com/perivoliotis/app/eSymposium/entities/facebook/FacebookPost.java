package com.perivoliotis.app.eSymposium.entities.facebook;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
public class FacebookPost {

    private String descriptionText;

    private String linkToMoreDescription;

    private int reactions;

    private int totalComments;

    private LocalDateTime timestamp;

    private List<String> videoUrls;

    private List<String> imagesUrls;

    private List<String> onlineUrls;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FacebookPost that = (FacebookPost) o;
        return Objects.equals(descriptionText, that.descriptionText) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptionText, timestamp);
    }
}
