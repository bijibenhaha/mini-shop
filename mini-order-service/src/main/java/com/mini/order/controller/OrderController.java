package com.mini.order.controller;

import com.mini.common.dto.OrderRequestDTO;
import com.mini.common.result.Result;
import com.mini.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public Result<Long> createOrder(@RequestBody OrderRequestDTO request) {
        Long orderId = orderService.createOrder(request);
        return Result.success(orderId);
    }
}