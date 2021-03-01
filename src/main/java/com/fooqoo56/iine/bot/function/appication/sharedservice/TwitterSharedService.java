package com.fooqoo56.iine.bot.function.appication.sharedservice;

import com.fooqoo56.iine.bot.function.domain.model.TweetCondition;
import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository;
import com.fooqoo56.iine.bot.function.exception.AlreadyFavoritedTweetException;
import com.fooqoo56.iine.bot.function.exception.InvalidTweetConditionException;
import com.fooqoo56.iine.bot.function.exception.NotFoundTweetException;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
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

            final List<TweetListResponse> tweetListResponses = new ArrayList<>();

            return Mono.just(payload)
                    // 一周目
                    .map(TweetRequest::buildTweetRequest)
                    .flatMap(twitterRepository::findTweet)
                    .doOnNext(tweetListResponses::add)
                    .map(TweetListResponse::getSearchMetaData)
                    // 二周目
                    .map(searchMetaData -> TweetRequest
                            .buildTweetRequest(payload, searchMetaData.getNextMaxId()))
                    .flatMap(twitterRepository::findTweet)
                    .doOnNext(tweetListResponses::add)
                    .map(TweetListResponse::getSearchMetaData)
                    // 三周目
                    .map(searchMetaData -> TweetRequest
                            .buildTweetRequest(payload, searchMetaData.getNextMaxId()))
                    .flatMap(twitterRepository::findTweet)
                    .doOnNext(tweetListResponses::add)
                    // リストのフラット処理
                    .thenReturn(tweetListResponses)
                    .map(this::flatTweetList);
        }

        throw new InvalidTweetConditionException("queryに値がありません");
    }

    /**
     * ツイートリストをフラットにする.
     *
     * @param tweetListResponseList ツイートAPIのレスポンスのリスト
     * @return ツイートのリスト
     */
    private List<TweetResponse> flatTweetList(final List<TweetListResponse> tweetListResponseList) {
        return tweetListResponseList.stream().map(TweetListResponse::getStatuses)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * ツイートをいいねする.
     *
     * @param ids ツイートIDのリスト
     * @throws AlreadyFavoritedTweetException 全てのツイートがすでにいいねされたツイートだった場合の例外
     */
    public Mono<TweetResponse> favoriteTweet(final List<String> ids)
            throws AlreadyFavoritedTweetException {

        return twitterRepository.lookupTweet(ids)
                .filter(response -> BooleanUtils.isFalse(response.getFavoriteFlag()))
                .collectList()
                .map(notFavoritedIds -> notFavoritedIds.stream().findFirst()
                        .orElseThrow(() -> new AlreadyFavoritedTweetException("直近のツイートは全ていいね済みです")))
                .map(TweetResponse::getId)
                .flatMap(twitterRepository::favoriteTweet);
    }
}
