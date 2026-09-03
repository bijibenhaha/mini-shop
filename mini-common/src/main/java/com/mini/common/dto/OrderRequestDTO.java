package com.mini.common.dto;

import lombok.Data;

@Data
public class OrderRequestDTO {

    private Long userId;
    private Long productId;
    private Integer count;
}