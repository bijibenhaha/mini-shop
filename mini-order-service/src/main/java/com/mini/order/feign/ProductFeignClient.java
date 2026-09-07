package com.mini.order.feign;

import com.mini.common.dto.DeductStockDTO;
import com.mini.common.dto.ProductDTO;
import com.mini.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mini-product-service", path = "/api/product")
public interface ProductFeignClient {

    @GetMapping("/{productId}")
    Result<ProductDTO> getProduct(@PathVariable("productId") Long productId);

    @PostMapping("/deduct/stock")
    Result<Void> deductStock(@RequestBody DeductStockDTO dto);
}