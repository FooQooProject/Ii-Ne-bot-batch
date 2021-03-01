package com.fooqoo56.iine.bot.function.infrastracture.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TweetResponse implements Serializable {

    private static final long serialVersionUID = 7312349288100389185L;

    @JsonProperty("id_str")
    private String id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("user")
    private User user;

    @JsonProperty("retweet_count")
    private Long retweetCount;

    @JsonProperty("favorite_count")
    private Long favoriteCount;

    @JsonProperty("favorited")
    private Boolean favoriteFlag;

    @JsonProperty("retweeted")
    private Boolean retweetFlag;

    @JsonProperty("possibly_sensitive")
    private Boolean sensitiveFlag;

    @JsonProperty("is_quote_status")
    private Boolean quoteFlag;

    @JsonProperty("in_reply_to_status_id_str")
    private String inReplyToStatusId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User implements Serializable {

        private static final long serialVersionUID = -2037788819579328427L;

        @JsonProperty("id_str")
        private String id;

        @JsonProperty("followers_count")
        private Long followersCount;

        @JsonProperty("friends_count")
        private Long friendsCount;

        @JsonProperty("listed_count")
        private Long listedCount;

        @JsonProperty("favourites_count")
        private Long favouritesCount;

        @JsonProperty("statuses_count")
        private Long statusesCount;

        @JsonProperty("following")
        private Boolean following;

        @JsonProperty("default_profile")
        private Boolean defaultProfileFlag;

        @JsonProperty("default_profile_image")
        private Boolean defaultProfileImageFlag;
    }
}
