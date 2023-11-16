package com.feng.fengchat.common.common.exception;

import com.feng.fengchat.common.common.domain.vo.response.ApiResult;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author jiangfeng
 * @date 2023/11/16
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException e){
        StringBuilder messageBuilder = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(m -> messageBuilder.append(m.getField())
                .append(": ")
                .append(m.getDefaultMessage())
                .append(","));
        String message = messageBuilder.toString();
        log.error("validation parameters error！The reason is:{}", e.getMessage(),e);
        return ApiResult.fail(CommonErrorEnum.PARAM_VALID.getErrorCode(),message.substring(0,message.length()-1));
    }

    /**
     * 兜底的异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResult systemExceptionHandler(Exception e){
        log.error("system exception！The reason is：{}", e.getMessage(), e);
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ApiResult BusinessExceptionHandler(Exception e){
        log.error("business exception！The reason is：{}", e.getMessage(), e);
        return ApiResult.fail(BusinessErrorEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }


}
