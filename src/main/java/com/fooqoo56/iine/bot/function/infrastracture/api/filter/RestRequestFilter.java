package com.fooqoo56.iine.bot.function.infrastracture.api.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestRequestFilter implements ExchangeFilterFunction {

    @NonNull
    @Override
    public Mono<ClientResponse> filter(final ClientRequest request, final ExchangeFunction next) {

        log.info(
                "api request - {}: {}", request.method(),
                request.url());
        log.info(
                "api request header: {}", request.headers().toString());

        return next.exchange(request)
                .doOnSuccess(clientResponse -> {
                    // response logging
                    log.info(
                            "api response - {}",
                            clientResponse.rawStatusCode()
                    );

                    log.info(
                            "api response header: {}", clientResponse.headers());
                });
    }
}
