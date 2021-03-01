package com.fooqoo56.iine.bot.function.appication.service;

import com.fooqoo56.iine.bot.function.appication.sharedservice.TwitterSharedService;
import com.fooqoo56.iine.bot.function.domain.model.TweetCondition;
import com.fooqoo56.iine.bot.function.exception.NotFoundTweetException;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class IiNeBotService {

    private static final int RETRY_NUM_OF_TWEET = 30;

    private final TwitterSharedService twitterSharedService;

    /**
     * Ii-Ne-Bot実行.
     *
     * @param query          クエリ
     * @param favoriteCount  いいね数
     * @param followersCount フォロワー数
     * @param friendsCount   フォロー数
     * @param retweetCount   リツイート数
     * @return RepeatStatus.FINISHED
     */
    @SuppressWarnings("checkstyle:WhitespaceAfter")
    @NonNull
    public RepeatStatus execute(final String query, final Long favoriteCount,
                                final Long followersCount, final Long friendsCount,
                                final Long retweetCount) {

        final TweetCondition payload =
                new TweetCondition(query, retweetCount, favoriteCount, followersCount,
                        friendsCount);

        Mono.just(payload)
                .flatMap(twitterSharedService::findTweet)
                .map(response -> {
                    final List<TweetResponse> tweetResponseList =
                            response.stream().filter(res -> isValidatedTweet(res, payload))
                                    .collect(Collectors.toList());

                    if (!tweetResponseList.isEmpty()) {
                        return tweetResponseList.stream().sorted(Comparator
                                .comparingLong(s -> s.getUser().getFavouritesCount()))
                                .map(TweetResponse::getId)
                                .limit(RETRY_NUM_OF_TWEET)
                                .collect(Collectors.toList());
                    } else {
                        throw new NotFoundTweetException("ツイートが見つかりませんでした");
                    }
                })
                .flatMap(twitterSharedService::favoriteTweet)
                .then()
                .block();

        return RepeatStatus.FINISHED;
    }

    /**
     * 条件に合致するツイートかどうかを判定.
     *
     * @param res       レスポンス
     * @param condition 条件
     * @return 条件に合致するツイートの場合、true
     */
    private boolean isValidatedTweet(final TweetResponse res, final TweetCondition condition) {

        if (Objects.isNull(res) || Objects.isNull(condition)) {
            return false;
        }

        if (notContainQuery(res.getText(), condition.getQuery())) {
            return false;
        }

        // favoriteレスポンスは常にfalseを出力
        return isGraterThan(res.getFavoriteCount(), condition.getFavoriteCount())
                && isGraterThan(res.getRetweetCount(), condition.getRetweetCount())
                && isGraterThan(res.getUser().getFollowersCount(), condition.getFollowersCount())
                && isGraterThan(res.getUser().getFriendsCount(), condition.getFriendsCount())
                && BooleanUtils.isFalse(res.getSensitiveFlag())
                && BooleanUtils.isFalse(res.getQuoteFlag())
                && StringUtils.isBlank(res.getInReplyToStatusId());
    }

    /**
     * 条件の単語がツイートに含まれるか判定する.
     *
     * @param text  ツイート
     * @param query 条件の単語
     * @return 条件の単語が含まれる場合、true
     */
    private boolean notContainQuery(final String text, final String query) {
        if (StringUtils.isBlank(text) || StringUtils.isBlank(query)) {
            return true;
        }

        return !text.contains(query);
    }

    /**
     * left > rightであるかどうか判定.
     *
     * @param left  left
     * @param right right
     * @return left > rightであれば、true
     */
    private boolean isGraterThan(final Long left, final Long right) {
        if (Objects.isNull(left) || Objects.isNull(right)) {
            return false;
        }

        return left >= right;
    }
}
