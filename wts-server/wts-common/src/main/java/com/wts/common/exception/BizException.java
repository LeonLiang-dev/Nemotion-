package com.wts.common.exception;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;

    public BizException(String message) {
        this(400, message);
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public static BizException fail(String message) {
        return new BizException(400, message);
    }

    public static BizException fail(int code, String message) {
        return new BizException(code, message);
    }

    public static BizException notFound(String entity) {
        return new BizException(404, entity + "不存在");
    }

    public static BizException forbidden(String message) {
        return new BizException(403, message);
    }
}
