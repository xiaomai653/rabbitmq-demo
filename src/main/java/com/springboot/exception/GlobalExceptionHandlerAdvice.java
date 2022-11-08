package com.springboot.exception;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {


    @ExceptionHandler(value = {DuplicateKeyException.class})
    public JSONObject duplicateKeyException(DuplicateKeyException ex) {
        log.error("primary key duplication exception:{}", ex.getMessage());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg","fail");
        return jsonObject;
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JSONObject exception() {
        log.error("exception");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg","fail");
        return jsonObject;
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JSONObject throwable() {
        log.error("throwable");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg","fail");
        return jsonObject;
    }



}
