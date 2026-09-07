package com.mini.order.config;

import com.mini.common.result.Result;
import com.mini.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        log.error("Business exception: ", e);  // ✅ 打印完整堆栈

        // 匹配已知业务异常
        String msg = e.getMessage();
        if (msg != null) {
            for (ResultCode code : ResultCode.values()) {
                if (code.getMsg().equals(msg)) {
                    return Result.error(code.getCode(), msg);
                }
            }
        }

        return Result.error(ResultCode.INTERNAL_ERROR.getCode(), msg != null ? msg : "unknown error");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("System exception: ", e);
        return Result.error(ResultCode.INTERNAL_ERROR.getMsg());
    }
}
