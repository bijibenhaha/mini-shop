package com.mini.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "success"),
    BAD_REQUEST(400, "bad request"),
    INTERNAL_ERROR(500, "internal server error"),
    SERVICE_UNAVAILABLE(503, "service unavailable"),

    // 业务错误码 5xx
    USER_NOT_FOUND(5001, "user not found"),
    INSUFFICIENT_BALANCE(5002, "insufficient balance"),
    PRODUCT_NOT_FOUND(5003, "product not found"),
    INSUFFICIENT_STOCK(5004, "insufficient stock"),
    ORDER_CREATE_FAILED(5005, "order create failed"),
    STOCK_DEDUCT_FAILED(5006, "stock deduct failed"),
    BALANCE_DEDUCT_FAILED(5007, "balance deduct failed"),

    // 限流熔断
    REQUEST_LIMITED(5010, "request too frequent, please try later"),
    SERVICE_DEGRADED(5011, "service busy, please try later");

    private final int code;
    private final String msg;
}