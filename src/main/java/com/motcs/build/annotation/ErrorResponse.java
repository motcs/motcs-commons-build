package com.motcs.build.annotation;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 该类为错误响应实体，实现 Serializable 接口
 *
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
public record ErrorResponse(String requestId, String path, Integer code, String message,
                            Object errors, LocalDateTime time) implements Serializable {
    public static ErrorResponse of(String requestId, String path, Integer code, String message, Object errors) {
        return new ErrorResponse(requestId, path, code, message, errors, LocalDateTime.now());
    }
}