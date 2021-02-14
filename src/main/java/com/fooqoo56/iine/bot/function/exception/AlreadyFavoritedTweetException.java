package com.fooqoo56.iine.bot.function.exception;

public class AlreadyFavoritedTweetException extends TwitterException {

    private static final long serialVersionUID = -8923043163393168770L;

    /**
     * デフォルトコンストラクタ.
     */
    public AlreadyFavoritedTweetException() {

    }

    /**
     * message引数コンストラクタ.
     *
     * @param message メッセージ
     */
    public AlreadyFavoritedTweetException(final String message) {
        super(message);
    }

    /**
     * コンストラクタ.
     *
     * @param message メッセージ
     * @param cause   Throwable
     */
    public AlreadyFavoritedTweetException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
