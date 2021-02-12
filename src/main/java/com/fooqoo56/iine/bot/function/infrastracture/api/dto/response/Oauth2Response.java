package com.fooqoo56.iine.bot.function.infrastracture.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class Oauth2Response implements Serializable {

    private static final long serialVersionUID = -7416129296418462389L;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("access_token")
    String accessToken;

    /**
     * デフォルトコンストラクタ.
     */
    public Oauth2Response() {
        tokenType = StringUtils.EMPTY;
        accessToken = StringUtils.EMPTY;
    }
}
