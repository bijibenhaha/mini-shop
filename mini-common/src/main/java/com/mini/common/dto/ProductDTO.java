package com.mini.common.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;
    private String productName;
    private Integer price;
    private Integer stock;
}