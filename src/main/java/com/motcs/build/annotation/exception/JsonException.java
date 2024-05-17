package com.motcs.build.annotation.exception;

import java.io.IOException;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
public class JsonException extends ServerException {

    /**
     * 构造函数，传入IOException异常
     *
     * @param exception IOException异常
     */
    public JsonException(IOException exception) {
        this(5001, exception);
    }

    /**
     * 构造函数，传入状态码和消息
     *
     * @param status 状态码
     * @param msg    消息
     */
    public JsonException(int status, Object msg) {
        super(status, "对象序列化Json失败!", msg);
    }

    /**
     * 静态方法，返回JsonException对象
     *
     * @param exception IOException异常
     * @return JsonException对象
     */
    public static JsonException withError(IOException exception) {
        return new JsonException(exception);
    }

}