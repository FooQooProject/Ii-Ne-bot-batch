package com.fooqoo56.iine.bot.function.exception;

public class InvalidTweetConditionException extends TwitterException {

    private static final long serialVersionUID = 3461737386400701200L;

    /**
     * message引数コンストラクタ.
     *
     * @param message メッセージ
     */
    public InvalidTweetConditionException(final String message) {
        super(message);
    }
}
