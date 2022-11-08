package com.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> {

    private String code;

    private String msg;

    private T data;

    public static Result success() {
        Result result = new Result();
        result.setCode("200").setMsg("success");
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setCode("500").setMsg("fail");
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setCode("500").setMsg("fail").setData(msg);
        return result;
    }

    public static Result success(String code, String msg) {
        Result result = new Result();
        result.setCode(code).setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result result = new Result();
        result.setCode("200").setMsg("success").setData(data);
        return result;
    }

}
