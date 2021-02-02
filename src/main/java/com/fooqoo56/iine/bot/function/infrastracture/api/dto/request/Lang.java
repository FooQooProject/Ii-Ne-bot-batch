package com.fooqoo56.iine.bot.function.infrastracture.api.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Lang {

    JA("ja"),
    EN("en");

    private final String country;
}
