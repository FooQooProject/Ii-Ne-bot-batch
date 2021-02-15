package com.fooqoo56.iine.bot.function.infrastracture.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TweetResponse implements Serializable {

    private static final long serialVersionUID = 7312349288100389185L;

    @JsonProperty("id_str")
    String id;

    @JsonProperty("text")
    String text;

    @JsonProperty("user")
    User user;

    @JsonProperty("retweet_count")
    Long retweetCount;

    @JsonProperty("favorite_count")
    Long favoriteCount;

    @JsonProperty("favorited")
    Boolean favoriteFlag;

    @JsonProperty("retweeted")
    Boolean retweetFlag;

    @JsonProperty("possibly_sensitive")
    Boolean sensitiveFlag;

    @JsonProperty("is_quote_status")
    Boolean quoteFlag;

    @JsonProperty("in_reply_to_status_id_str")
    String inReplyToStatusId;

    /**
     * デフォルトコンストラクタ.
     */
    public TweetResponse() {
        id = StringUtils.EMPTY;
        text = StringUtils.EMPTY;
        user = new User();
        retweetCount = 0L;
        favoriteCount = 0L;
        favoriteFlag = false;
        retweetFlag = false;
        sensitiveFlag = false;
        quoteFlag = false;
        inReplyToStatusId = StringUtils.EMPTY;
    }

    @Value
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User implements Serializable {

        private static final long serialVersionUID = -2037788819579328427L;

        @JsonProperty("id_str")
        String id;

        @JsonProperty("followers_count")
        Long followersCount;

        @JsonProperty("friends_count")
        Long friendsCount;

        @JsonProperty("listed_count")
        Long listedCount;

        @JsonProperty("favourites_count")
        Long favouritesCount;

        @JsonProperty("statuses_count")
        Long statusesCount;

        @JsonProperty("following")
        Boolean following;

        @JsonProperty("default_profile")
        Boolean defaultProfileFlag;

        @JsonProperty("default_profile_image")
        Boolean defaultProfileImageFlag;

        /**
         * デフォルトコンストラクタ.
         */
        public User() {
            id = StringUtils.EMPTY;
            followersCount = 0L;
            friendsCount = 0L;
            listedCount = 0L;
            favouritesCount = 0L;
            statusesCount = 0L;
            following = false;
            defaultProfileFlag = true;
            defaultProfileImageFlag = true;
        }
    }
}
