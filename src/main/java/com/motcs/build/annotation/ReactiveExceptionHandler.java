package com.motcs.build.annotation;

import com.motcs.build.annotation.exception.ClientException;
import com.motcs.build.annotation.exception.ServerException;
import io.r2dbc.spi.R2dbcException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.r2dbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
@Log4j2
@ControllerAdvice
public class ReactiveExceptionHandler {

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> handleBindException(ServerWebExchange exchange, ServerWebInputException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        if (ex instanceof WebExchangeBindException bindException) {
            errors = bindException.getBindingResult().getAllErrors().parallelStream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        } else {
            errors.add(Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(ex.getMessage()));
            errors.add(ex.getReason());
        }
        log.error("ServerWebInputException 请求参数验证失败!", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getRequest().getId(), exchange.getRequest().getPath().value(),
                        4071, "请求参数验证失败!", errors));
    }

    @ExceptionHandler({DataAccessException.class, R2dbcException.class})
    public ResponseEntity<ErrorResponse> handleLockingFailureException(ServerWebExchange exchange, RuntimeException ex) {
        List<String> errors = new ArrayList<>();
        if (ex instanceof R2dbcException r2dbcException) {
            errors.add(r2dbcException.getLocalizedMessage());
            errors.add(r2dbcException.getSql());
            errors.add(r2dbcException.getSqlState());
        } else if (ex instanceof BadSqlGrammarException grammarException) {
            errors.add(grammarException.getLocalizedMessage());
            errors.add(grammarException.getSql());
        } else {
            errors.add(ex.getLocalizedMessage());
        }
        log.error("DataAccessException 数据库操作失败!", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getLogPrefix(), exchange.getRequest().getPath().value(),
                        5071, "数据库操作失败!", errors));
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handleClientException(ServerWebExchange exchange, ClientException ex) {
        log.error("ClientException 内部服务访问错误!", ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getLogPrefix(), exchange.getRequest().getPath().value(), ex.getCode(),
                        "%s 内部服务访问错误!".formatted(ex.getServiceId()), List.of(ex.getMessage(), ex.getMsg())));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponse> handleRestServerException(ServerWebExchange exchange, ServerException ex) {
        log.error("ServerException 服务器自定义错误!", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getLogPrefix(), exchange.getRequest().getPath().value(), ex.getCode(),
                        ex.getMessage(), ex.getMsg()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(ServerWebExchange exchange, Exception ex) {
        log.error("Exception 服务器未知错误!", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(exchange.getLogPrefix(), exchange.getRequest().getPath().value(),
                        5000, "服务器未知错误!", List.of(ex.getMessage())));
    }

}