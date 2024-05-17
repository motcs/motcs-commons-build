package com.motcs.build.annotation;

import java.io.Serializable;

/**
 * 该类为错误响应实体，实现 Serializable 接口
 *
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
public record SuccessResponse(Integer code, String message) implements Serializable {

    public static SuccessResponse of(Integer code, String message) {
        return new SuccessResponse(code, message);
    }

}