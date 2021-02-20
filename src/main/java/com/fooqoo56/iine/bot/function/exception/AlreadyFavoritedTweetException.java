package com.fooqoo56.iine.bot.function.exception;

public class AlreadyFavoritedTweetException extends TwitterException {

    private static final long serialVersionUID = -8923043163393168770L;

    /**
     * message引数コンストラクタ.
     *
     * @param message メッセージ
     */
    public AlreadyFavoritedTweetException(final String message) {
        super(message);
    }
}
