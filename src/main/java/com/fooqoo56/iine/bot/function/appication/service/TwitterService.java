package com.fooqoo56.iine.bot.function.appication.service;

import com.fooqoo56.iine.bot.function.domain.model.TweetCondition;
import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository;
import com.fooqoo56.iine.bot.function.exception.AlreadyFavoritedTweetException;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwitterService {

    private static final String NO_MORE_TWEET_ID = "0";

    private final TwitterRepository twitterRepository;

    /**
     * ツイートを検索する.
     *
     * @param payload 検索条件
     * @return ツイートレスポンス
     */
    @NonNull
    public List<TweetResponse> findTweet(final TweetCondition payload) {

        if (StringUtils.isNoneBlank(payload.getQuery())) {

            final List<TweetListResponse> tweetListResponses = new ArrayList<>();

            twitterRepository
                    .findTweet(TweetRequest.convertPayloadToRequest(payload))
                    .map(tweetListResponses::add)
                    .subscribe();

            for (int idx = 1; idx <= 5; idx++) {
                if (StringUtils
                        .isBlank(tweetListResponses.get(idx - 1).getSearchMetaData()
                                .getNextMaxId())
                        || NO_MORE_TWEET_ID
                        .equals(tweetListResponses.get(idx - 1).getSearchMetaData()
                                .getNextMaxId())) {
                    break;
                }

                twitterRepository.findTweet(TweetRequest
                        .convertPayloadToRequestWithPayload(payload,
                                tweetListResponses.get(idx - 1).getSearchMetaData()
                                        .getNextMaxId()))
                        .map(tweetListResponses::add)
                        .subscribe();
            }

            return tweetListResponses.stream().map(TweetListResponse::getStatuses)
                    .flatMap(Collection::stream).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * ツイートをいいねする.
     *
     * @param tweetIds ツイートIDのリスト
     * @throws AlreadyFavoritedTweetException 全てのツイートがすでにいいねされたツイートだった場合の例外
     */
    public void favoriteTweet(final List<String> tweetIds)
            throws AlreadyFavoritedTweetException {

        for (final String id : tweetIds) {
            if (StringUtils.isNoneBlank(id)) {
                try {
                    twitterRepository.favoriteTweet(id).subscribe();
                } catch (final RuntimeException exception) {
                    log.warn(exception.getMessage());
                }
            }
        }

        throw new AlreadyFavoritedTweetException();
    }
}
