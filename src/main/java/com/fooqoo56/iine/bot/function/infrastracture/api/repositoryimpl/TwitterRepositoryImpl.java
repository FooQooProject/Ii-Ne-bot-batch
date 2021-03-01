package com.fooqoo56.iine.bot.function.infrastracture.api.repositoryimpl;

import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository;
import com.fooqoo56.iine.bot.function.infrastracture.api.config.ApiSetting;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.Oauth2Response;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetResponse;
import com.fooqoo56.iine.bot.function.infrastracture.api.util.OauthAuthorizationHeaderBuilder;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
@RequiredArgsConstructor
public class TwitterRepositoryImpl implements TwitterRepository {

    private static final String BEARER_PREFIX = "Bearer ";

    private final ApiSetting twitterFavoriteApiSetting;

    private final WebClient twitterSearchClient;
    private final WebClient twitterFavoriteClient;
    private final WebClient bearerTokenTwitterClient;
    private final WebClient twitterLookupClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TweetListResponse> findTweet(final TweetRequest request) {
        return getBearerToken()
                .map(Oauth2Response::getAccessToken)
                .flatMap(
                        accessToken -> twitterSearchClient
                                .get()
                                .uri(uriBuilder -> uriBuilder.queryParams(request.getQueryMap())
                                        .build())
                                .header(HttpHeaders.AUTHORIZATION,
                                        BEARER_PREFIX + accessToken)
                                .retrieve()
                                .bodyToMono(TweetListResponse.class)
                );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TweetResponse> favoriteTweet(final String id) {
        return twitterFavoriteClient
                .post()
                .uri(uriBuilder -> uriBuilder.queryParam("id", id).build())
                .header(HttpHeaders.AUTHORIZATION, getOauth2Header(id))
                .retrieve()
                .bodyToMono(TweetResponse.class);


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<TweetResponse> lookupTweet(final List<String> ids) {
        final String id = String.join(",", ids);
        return getBearerToken()
                .map(Oauth2Response::getAccessToken)
                .flatMapMany(
                        accessToken -> twitterLookupClient
                                .get()
                                .uri(uriBuilder -> uriBuilder.queryParam("id", String.join(",", ids))
                                        .build())
                                .header(HttpHeaders.AUTHORIZATION,
                                        BEARER_PREFIX + accessToken)
                                .retrieve()
                                .bodyToFlux(TweetResponse.class));
    }

    /**
     * トークン取得.
     *
     * @return Oauth2Response
     */
    private Mono<Oauth2Response> getBearerToken() {

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        return bearerTokenTwitterClient
                .post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Oauth2Response.class);
    }

    /**
     * Oauth2ヘッダ取得.
     *
     * @param id ツイートID
     * @return Oauth2ヘッダ
     */
    @NonNull
    private String getOauth2Header(final String id) {
        return OauthAuthorizationHeaderBuilder
                .builder()
                .method(HttpMethod.POST.name().toUpperCase())
                .url(twitterFavoriteApiSetting.getBaseUrl())
                .consumerSecret(twitterFavoriteApiSetting.getApiSecret())
                .tokenSecret(twitterFavoriteApiSetting.getAccessTokenSecret())
                .accessToken(twitterFavoriteApiSetting.getAccessToken())
                .consumerKey(twitterFavoriteApiSetting.getApikey())
                .queryParameters(Map.of("id", id))
                .build().getOauthHeader();
    }
}
