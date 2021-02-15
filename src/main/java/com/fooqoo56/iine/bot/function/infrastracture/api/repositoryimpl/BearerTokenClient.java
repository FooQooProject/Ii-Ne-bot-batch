package com.fooqoo56.iine.bot.function.infrastracture.api.repositoryimpl;

import com.fooqoo56.iine.bot.function.infrastracture.api.config.BearerTokenClientConfig;
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.Oauth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class BearerTokenClient {

    private final BearerTokenClientConfig config;
    private final WebClient bearerTokenTwitterClient;

    /**
     * トークン取得.
     *
     * @return Oauth2Response
     */
    public Mono<Oauth2Response> getBearerToken() {

        return bearerTokenTwitterClient
                .post()
                .uri(config.getPath())
                .bodyValue(getBody())
                .retrieve()
                .bodyToMono(Oauth2Response.class);
    }

    /**
     * Oauth2の必須パラメータ取得.
     *
     * @return MultiValueMap
     */
    @NonNull
    private MultiValueMap<String, String> getBody() {
        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        return body;
    }
}
