package com.fooqoo56.iine.bot.function.exception;

public class SpringBatchException extends Exception {


    private static final long serialVersionUID = -5624518839842556295L;

    /**
     * デフォルトコンストラクタ.
     */
    public SpringBatchException() {

    }

    /**
     * コンストラクタ.
     *
     * @param message メッセージ
     */
    public SpringBatchException(final String message) {
        super(message);
    }

    /**
     * コンストラクタ.
     *
     * @param message メッセージ
     * @param cause   例外
     */
    public SpringBatchException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
