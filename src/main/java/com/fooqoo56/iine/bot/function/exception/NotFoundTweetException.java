package com.fooqoo56.iine.bot.function.exception;

public class NotFoundTweetException extends TwitterException {

    private static final long serialVersionUID = 2372240258976640838L;

    /**
     * デフォルトコンストラクタ.
     */
    public NotFoundTweetException() {

    }

    /**
     * message引数コンストラクタ.
     *
     * @param message メッセージ
     */
    public NotFoundTweetException(final String message) {
        super(message);
    }

    /**
     * コンストラクタ.
     *
     * @param message メッセージ
     * @param cause Throwable
     */
    public NotFoundTweetException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
