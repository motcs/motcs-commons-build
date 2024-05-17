package com.motcs.build.mvc;

import com.motcs.build.annotation.ErrorResponse;
import com.motcs.build.annotation.exception.ClientException;
import com.motcs.build.annotation.exception.ServerException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author billb
 */
@Log4j2
@ControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(HttpServletRequest exchange, BindException ex) {
        List<String> errors = new ArrayList<>();
        if (ex instanceof MethodArgumentNotValidException bindException) {
            errors = bindException.getBindingResult().getAllErrors().parallelStream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        } else {
            errors.add(Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(ex.getMessage()));
            errors.add(ex.getObjectName());
        }
        log.error("BindException 请求参数验证失败!", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getRequestId(), exchange.getPathInfo(),
                        4071, "请求参数验证失败!", errors));
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ErrorResponse> servletException(HttpServletRequest exchange, ServletException ex) {
        List<String> errors = new ArrayList<>();
        if (ex instanceof ServletRequestBindingException bindException) {
            errors = Arrays.stream(Objects.requireNonNull(bindException.getDetailMessageArguments()))
                    .map(Object::toString).collect(Collectors.toList());
        } else {
            errors.add(Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(ex.getMessage()));
        }
        log.error("ServletException 请求参数验证失败!", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getRequestId(), exchange.getPathInfo(),
                        4071, "请求参数验证失败!", errors));
    }


    /**
     * 处理数据库操作异常
     */
    @ExceptionHandler({NestedRuntimeException.class})
    public ResponseEntity<ErrorResponse> handleLockingFailureException(HttpServletRequest exchange, NestedRuntimeException ex) {
        List<String> errors = new ArrayList<>();
        if (ex instanceof BadSqlGrammarException grammarException) {
            errors.add(grammarException.getLocalizedMessage());
            errors.add(grammarException.getSql());
        } else {
            errors.add(ex.getLocalizedMessage());
        }
        log.error("NestedRuntimeException 数据库操作错误!", ex);
        return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getRequestId(), exchange.getPathInfo(),
                        5071, "数据库操作错误!", errors));
    }

    /**
     * 处理客户端请求异常
     */
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handleClientException(HttpServletRequest exchange, ClientException ex) {
        log.error("ClientException 内部服务访问错误!", ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getRequestId(), exchange.getPathInfo(),
                        ex.getCode(), "内部服务访问错误! 服务: [" + ex.getServiceId() + "]", ex.getMsg()));
    }

    /**
     * 处理服务器自定义异常
     */
    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponse> handleRestServerException(HttpServletRequest exchange, ServerException ex) {
        log.error("ServerException 服务器自定义错误!", ex);
        String errorMessage = Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getRequestId(), exchange.getPathInfo(),
                        ex.getCode(), StringUtils.hasLength(errorMessage) ? errorMessage : "服务器自定义错误!", ex.getMsg()));
    }

    /**
     * 处理未知异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(HttpServletRequest exchange, Exception ex) {
        log.error("Exception 服务器未知错误!", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getRequestId(), exchange.getPathInfo(),
                        5000, "服务器未知错误!", List.of(ex.getMessage())));
    }

}