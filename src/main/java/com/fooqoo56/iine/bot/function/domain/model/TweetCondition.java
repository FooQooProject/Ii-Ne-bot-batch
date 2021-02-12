package com.fooqoo56.iine.bot.function.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TweetCondition implements Serializable {

    private static final long serialVersionUID = 5371709977743888346L;

    @NonNull
    private final String query;

    @NonNull
    private final Integer retweetCount;

    @NonNull
    private final Integer favoriteCount;

    @NonNull
    private final Integer followersCount;

    @NonNull
    private final Integer friendsCount;

    /**
     * Json生成.
     *
     * @param query          クエリ
     * @param retweetCount   リツイート数
     * @param favoriteCount  いいね数
     * @param followersCount フォロワー数
     * @param friendsCount   フォロー数
     */
    @JsonCreator
    public TweetCondition(
            @JsonProperty("query") final String query,
            @JsonProperty("retweetCount") final Integer retweetCount,
            @JsonProperty("favoriteCount") final Integer favoriteCount,
            @JsonProperty("followersCount") final Integer followersCount,
            @JsonProperty("friendsCount") final Integer friendsCount
    ) {
        this.query = StringUtils.defaultString(query);
        this.retweetCount = retweetCount;
        this.favoriteCount = favoriteCount;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
    }
}
