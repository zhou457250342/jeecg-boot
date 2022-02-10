package org.jeecg.modules.rec.engine.model;

/**
 * rec 异常
 *
 * @Author: zhou x
 * @Date: 2022/2/10 12:14
 */
public class RecException extends RuntimeException {
    public RecException() {
        super();
    }

    public RecException(String message) {
        super(message);
    }

    public RecException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecException(Throwable cause) {
        super(cause);
    }

    protected RecException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
