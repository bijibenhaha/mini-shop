package com.mini.product.service.impl;

import com.mini.common.dto.ProductDTO;
import com.mini.common.result.ResultCode;
import com.mini.product.entity.Product;
import com.mini.product.mapper.ProductMapper;
import com.mini.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException(ResultCode.PRODUCT_NOT_FOUND.getMsg());
        }
        return toDTO(product);
    }

    @Override
    public void deductStock(Long productId, Integer count) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException(ResultCode.PRODUCT_NOT_FOUND.getMsg());
        }
        if (product.getStock() < count) {
            throw new RuntimeException(ResultCode.INSUFFICIENT_STOCK.getMsg());
        }
        int rows = productMapper.deductStock(productId, count);
        if (rows <= 0) {
            throw new RuntimeException(ResultCode.STOCK_DEDUCT_FAILED.getMsg());
        }
        log.info("Product {} stock deducted: {}, remaining: {}", productId, count, product.getStock() - count);
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        return dto;
    }
}