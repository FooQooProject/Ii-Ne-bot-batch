package com.fooqoo56.iine.bot.function.appication.sharedservice;

import com.fooqoo56.iine.bot.function.domain.model.TweetCondition;
import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository;
import com.fooqoo56.iine.bot.function.exception.AlreadyFavoritedTweetException;
import com.fooqoo56.iine.bot.function.exception.InvalidTweetConditionException;
import com.fooqoo56.iine.bot.function.exception.NotFoundTweetException;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetResponse;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwitterSharedService {

    private static final String NO_MORE_TWEET_ID = "0";

    private final TwitterRepository twitterRepository;

    /**
     * ツイートを検索する.
     *
     * @param payload 検索条件
     * @return ツイートレスポンス
     * @throws InvalidTweetConditionException ツイート条件が異常な場合の例外
     * @throws NotFoundTweetException         ツイートが存在しない場合の例外
     */
    @NonNull
    public Mono<List<TweetResponse>> findTweet(final TweetCondition payload)
            throws InvalidTweetConditionException {

        if (StringUtils.isNoneBlank(payload.getQuery())) {

            // グローバルなリクエスト
            final TweetRequest request = TweetRequest.buildTweetRequest(payload);

            return Flux.range(1, 5)
                    .flatMap(idx -> {
                        if ("0".equals(request.getMaxId())) {
                            return Mono.just(new TweetListResponse());
                        } else {
                            return twitterRepository.findTweet(request)
                                    .doOnNext(res -> request.setMaxId(
                                            res.getSearchMetaData().getNextMaxId()));
                        }
                    })
                    .collectList()
                    .map(responses -> responses.stream().map(TweetListResponse::getStatuses)
                            .flatMap(Collection::stream).collect(Collectors.toList()));
        }

        throw new InvalidTweetConditionException("queryに値がありません");
    }

    /**
     * ツイートをいいねする.
     *
     * @param tweetIds ツイートIDのリスト
     * @throws AlreadyFavoritedTweetException 全てのツイートがすでにいいねされたツイートだった場合の例外
     */
    public Mono<TweetResponse> favoriteTweet(final List<String> tweetIds)
            throws AlreadyFavoritedTweetException {

        for (final String id : tweetIds) {
            if (StringUtils.isNoneBlank(id)) {
                return twitterRepository.favoriteTweet(id);
            }
        }

        throw new AlreadyFavoritedTweetException("ツイートはすでにいいね済です");
    }
}
