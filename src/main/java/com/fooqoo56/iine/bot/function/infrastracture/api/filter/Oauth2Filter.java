package com.fooqoo56.iine.bot.function.infrastracture.api.filter;

import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.Oauth2Response;
import com.fooqoo56.iine.bot.function.infrastracture.api.repositoryimpl.BearerTokenClient;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Oauth2Filter implements ExchangeFilterFunction {

    private static final String BEARER_PREFIX = "Bearer ";
    private final BearerTokenClient bearerTokenClient;

    @NonNull
    @Override
    public Mono<ClientResponse> filter(
            @NonNull final ClientRequest request,
            @NonNull final ExchangeFunction next) {
        final ClientRequest wrappedRequest = ClientRequest.from(request)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .build();

        return next.exchange(wrappedRequest);
    }

    /**
     * BearerのTokenを取得する.
     *
     * @return Bearerの取得
     */
    @NonNull
    private String getBearerToken() {
        final Oauth2Response response = bearerTokenClient.getBearerToken();

        if (Objects.nonNull(response)) {
            return BEARER_PREFIX + response.getAccessToken();
        }

        throw new RuntimeException();
    }
}
