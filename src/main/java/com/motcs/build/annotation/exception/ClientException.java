package com.motcs.build.annotation.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
@Setter
@Getter
public class ClientException extends ServerException {

    /**
     * -- GETTER --
     * 获取serviceId
     * -- SETTER --
     * 设置serviceId
     * <p>
     * serviceId 服务ID
     */
    private String serviceId;

    public ClientException(int code, String message, Object msg) {
        super(code, message, msg);
    }

    /**
     * 根据code和msg创建ClientRequestException
     *
     * @param code   异常码
     * @param errors 异常信息
     * @return ClientException
     */
    public static ClientException withMsg(int code, String message, Object errors) {
        return new ClientException(code, message, errors);
    }

    /**
     * 根据msg创建ClientRequestException
     *
     * @param errors 异常信息
     * @return ClientException
     */
    public static ClientException withMsg(Object errors) {
        return withMsg(5020, "内部服务访问失败!", errors);
    }

    /**
     * 设置serviceId
     *
     * @param serviceId 服务ID
     * @return ClientException
     */
    public ClientException serviceId(String serviceId) {
        this.setServiceId(serviceId);
        return this;
    }

}