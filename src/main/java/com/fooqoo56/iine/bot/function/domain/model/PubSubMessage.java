package com.fooqoo56.iine.bot.function.domain.model;

import java.io.Serializable;
import java.util.Map;
import lombok.Data;
import org.springframework.lang.NonNull;

/**
 * pubsubメッセージのクラス.
 */
@Data
public class PubSubMessage implements Serializable {

    private static final long serialVersionUID = 2724247698103283771L;

    @NonNull
    private final String data;

    @NonNull
    private final Map<String, String> attributes;

    @NonNull
    private final String messageId;

    @NonNull
    private final String publishTime;
}
