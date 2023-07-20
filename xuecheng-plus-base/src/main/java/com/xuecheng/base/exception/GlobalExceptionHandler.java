package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zzy
 * @description:  全局异常处理
 * @date: 2023/7/20 17:28
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //对项目的自定义异常类型进行处理
    @ExceptionHandler(value = XueChengPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customExceptionHandler(XueChengPlusException e) {
        //记录异常
        log.error("系统异常{}", e.getErrMessage(), e);
        //返回异常结果数据
        return new RestErrorResponse(e.getErrMessage());
    }
    //对系统异常类型进行处理
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse systemExceptionHandler(Exception e) {
        //记录异常
        log.error("系统异常{}", e.getMessage(), e);
        //返回异常结果数据
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
    }
    //对校验规则异常类型进行处理
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        //存储错误信息
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().stream().forEach(item -> errors.add(item.getDefaultMessage()));
        //将list的错误信息拼接起来
        String errMessage = StringUtils.join(errors, ",");

        //记录异常
        log.error("系统异常{}", e.getMessage(), e);
        //返回异常结果数据
        return new RestErrorResponse(errMessage);
    }
}
