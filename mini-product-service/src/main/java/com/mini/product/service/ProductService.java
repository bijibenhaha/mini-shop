package com.mini.product.service;

import com.mini.common.dto.ProductDTO;

public interface ProductService {

    ProductDTO getProductById(Long productId);

    void deductStock(Long productId, Integer count);
}