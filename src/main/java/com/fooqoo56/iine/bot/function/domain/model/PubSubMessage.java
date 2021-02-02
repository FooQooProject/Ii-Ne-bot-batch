package com.fooqoo56.iine.bot.function.domain.model;

import java.io.Serializable;
import java.util.Map;
import lombok.Data;

/**
 * pubsubメッセージのクラス.
 */
@Data
public class PubSubMessage implements Serializable {

    private static final long serialVersionUID = 2724247698103283771L;

    private String data;

    private Map<String, String> attributes;

    private String messageId;

    private String publishTime;
}
