package com.fooqoo56.iine.bot.function.infrastracture.api.config;

import java.io.Serializable;
import java.time.Duration;
import lombok.Data;

@Data
public class ApiSetting implements Serializable {

    private static final long serialVersionUID = -7928883599849129581L;

    private String baseUrl;

    private String path;

    private Duration connectTimeout;

    private Duration readTimeout;

    private String apikey;

    private String apiSecret;

    private String accessToken;

    private String accessTokenSecret;

    private Integer maxInMemorySize;
}
