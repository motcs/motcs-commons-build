package com.motcs.build.annotation.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerException extends RuntimeException implements Serializable {

    protected Object msg;

    protected int code;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(int code, String message, Object errors) {
        super(message);
        this.msg = errors;
        this.code = code;
    }

    public static ServerException withMsg(int code, String message, Object errors) {
        return new ServerException(code, message, errors);
    }

    public static ServerException withMsg(String message, Object errors) {
        return ServerException.withMsg(1500, message, errors);
    }

    public static ServerException withMsg(Object errors) {
        return ServerException.withMsg(1500, "服务自定义错误!", errors);
    }

    public static ServerException withMsg(int code, String message) {
        return ServerException.withMsg(code, message, List.of());
    }

}