package com.fooqoo56.iine.bot.function.appication.job.twitter.tasklet;

import com.fooqoo56.iine.bot.function.exception.NotFoundTweetException;
import com.fooqoo56.iine.bot.function.appication.service.TwitterService;
import com.fooqoo56.iine.bot.function.domain.model.TweetCondition;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwitterTasklet implements Tasklet {

    private static final int RETRY_NUM_OF_TWEET = 10;

    private final TwitterService twitterService;

    /**
     * Twitterタスクレット.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     */
    @Override
    @NonNull
    public RepeatStatus execute(@NonNull final StepContribution contribution,
                                @NonNull final ChunkContext chunkContext) {

        final TweetCondition payload = new TweetCondition("Next.js", 3, 3, 10, 10);

        final List<TweetResponse> response = twitterService.findTweet(payload);
        final List<String> tweetIds =
                response.stream().filter(res -> isValidatedTweet(res, payload))
                        .sorted(Comparator.comparingInt(s -> s.getUser().getFavouritesCount()))
                        .map(TweetResponse::getId)
                        .limit(RETRY_NUM_OF_TWEET)
                        .collect(Collectors.toList());

        if (tweetIds.isEmpty()) {
            throw new NotFoundTweetException();
        }

        twitterService.favoriteTweet(tweetIds);

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
                && !res.getSensitiveFlag()
                && !res.getQuoteFlag()
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
    private boolean isGraterThan(final Integer left, final Integer right) {
        if (Objects.isNull(left) || Objects.isNull(right)) {
            return false;
        }

        return left >= right;
    }
}
