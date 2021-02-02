package com.fooqoo56.iine.bot.function.domain.repository.api;

import com.fooqoo56.iine.bot.function.infrastracture.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetResponse;

public interface TwitterRepository {

    /**
     * フォロワー取得.
     *
     * @param request TweetRequest
     * @return TwitterFollowerResponse
     */
    TweetListResponse findTweet(final TweetRequest request);

    /**
     * ツイートのいいね.
     *
     * @param id ツイートID
     * @return TweetResponse
     */
    TweetResponse favoriteTweet(final String id);
}
