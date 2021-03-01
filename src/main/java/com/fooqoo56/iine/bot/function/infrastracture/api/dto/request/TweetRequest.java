package com.fooqoo56.iine.bot.function.infrastracture.api.dto.request;

import com.fooqoo56.iine.bot.function.domain.model.TweetCondition;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.constant.Lang;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.constant.ResultType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@Builder
public class TweetRequest implements Serializable {

    private static final long serialVersionUID = 4303034636519921068L;

    private static final String DEFAULT_MAX_ID = "-1";

    @NonNull
    private final String query;

    @NonNull
    private final Lang lang = Lang.JA;

    @NonNull
    private final ResultType resultType = ResultType.RECENT;

    @NonNull
    private final Integer count = 100;

    @NonNull
    private final Boolean includeEntitiesFlag = false;

    @NonNull
    private final LocalDate until = LocalDate.now();

    @NonNull
    private String maxId;

    /**
     * payloadをAPIクエリへ変換.
     *
     * @param payload PayLoad
     * @return APIクエリ
     */
    public static TweetRequest buildTweetRequest(final TweetCondition payload) {
        return TweetRequest
                .builder()
                .query(addFilterRetweet(payload.getQuery()))
                .maxId(DEFAULT_MAX_ID)
                .build();
    }

    /**
     * payloadをAPIクエリへ変換.
     *
     * @param payload   PayLoad
     * @param nextMaxId nextMaxId
     * @return APIクエリ
     */
    public static TweetRequest buildTweetRequest(final TweetCondition payload,
                                                 final String nextMaxId) {
        return TweetRequest
                .builder()
                .query(addFilterRetweet(payload.getQuery()))
                .maxId(nextMaxId)
                .build();
    }

    /**
     * クエリにリツイートを除くフィルタを追加する.
     *
     * @param query クエリ
     * @return フィルタ付きクエリ
     */
    private static String addFilterRetweet(final String query) {
        return query + " -filter:retweets";
    }

    /**
     * クエリをMap型に変換.
     *
     * @return Map
     */
    public MultiValueMap<String, String> getQueryMap() {
        final MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();

        queries.add("q", query);
        queries.add("lang", lang.getCountry());
        queries.add("result_type", resultType.getName());
        queries.add("count", count.toString());
        queries.add("include_entities", includeEntitiesFlag.toString());
        queries.add("until", until.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queries.add("max_id", maxId);

        return queries;
    }
}
