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
    private String data;

    @NonNull
    private Map<String, String> attributes;

    @NonNull
    private String messageId;

    @NonNull
    private String publishTime;
}
