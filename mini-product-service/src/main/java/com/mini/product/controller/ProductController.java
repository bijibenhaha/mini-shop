package com.mini.product.controller;

import com.mini.common.dto.DeductStockDTO;
import com.mini.common.dto.ProductDTO;
import com.mini.common.result.Result;
import com.mini.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public Result<ProductDTO> getProduct(@PathVariable Long productId) {
        ProductDTO product = productService.getProductById(productId);
        return Result.success(product);
    }

    @PostMapping("/deduct/stock")
    public Result<Void> deductStock(@RequestBody DeductStockDTO dto) {
        productService.deductStock(dto.getProductId(), dto.getCount());
        return Result.success();
    }
}