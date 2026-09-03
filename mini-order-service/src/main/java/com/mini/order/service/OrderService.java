package com.mini.order.service;

import com.mini.common.dto.OrderRequestDTO;

public interface OrderService {

    Long createOrder(OrderRequestDTO request);
}