package com.fooqoo56.iine.bot.function.exception;

public abstract class TwitterException extends RuntimeException {

    private static final long serialVersionUID = -8870701197622795497L;

    /**
     * message引数コンストラクタ.
     *
     * @param message メッセージ
     */
    public TwitterException(final String message) {
        super(message);
    }

    /**
     * コンストラクタ.
     *
     * @param message メッセージ
     * @param cause   Throwable
     */
    public TwitterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
