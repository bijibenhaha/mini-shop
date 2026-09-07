package com.mini.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.mini.common.dto.DeductBalanceDTO;
import com.mini.common.dto.DeductStockDTO;
import com.mini.common.dto.OrderRequestDTO;
import com.mini.common.dto.ProductDTO;
import com.mini.common.result.ResultCode;
import com.mini.order.entity.Order;
import com.mini.order.feign.ProductFeignClient;
import com.mini.order.feign.UserFeignClient;
import com.mini.order.mapper.OrderMapper;
import com.mini.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final UserFeignClient userFeignClient;
    private final ProductFeignClient productFeignClient;

    @Override
    @SentinelResource(value = "createOrder",
            blockHandler = "createOrderBlockHandler",
            fallback = "createOrderFallback")
    public Long createOrder(OrderRequestDTO request) {
        Long userId = request.getUserId();
        Long productId = request.getProductId();
        Integer count = request.getCount();

        // 1. 查询商品信息及价格
        ProductDTO product = productFeignClient.getProduct(productId).getData();
        if (product == null) {
            throw new RuntimeException(ResultCode.PRODUCT_NOT_FOUND.getMsg());
        }
        log.info("Product info: {}, price: {}, stock: {}", product.getProductName(), product.getPrice(), product.getStock());

        // 2. 计算总金额
        int totalPrice = product.getPrice() * count;

        // 3. 扣减库存 (Feign 调用 product-service)
        DeductStockDTO deductStockDTO = new DeductStockDTO();
        deductStockDTO.setProductId(productId);
        deductStockDTO.setCount(count);
        productFeignClient.deductStock(deductStockDTO);
        log.info("Stock deducted: productId={}, count={}", productId, count);

        // 4. 扣减余额 (Feign 调用 user-service)
        DeductBalanceDTO deductBalanceDTO = new DeductBalanceDTO();
        deductBalanceDTO.setUserId(userId);
        deductBalanceDTO.setAmount(totalPrice);
        userFeignClient.deductBalance(deductBalanceDTO);
        log.info("Balance deducted: userId={}, amount={}", userId, totalPrice);

        // 5. 本地插入订单
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setCount(count);
        order.setTotalPrice(totalPrice);
        order.setStatus(1); // 1-已创建
        orderMapper.insert(order);

        log.info("Order created successfully: orderId={}, userId={}, productId={}, count={}, totalPrice={}",
                order.getId(), userId, productId, count, totalPrice);

        return order.getId();
    }

    /**
     * Sentinel 限流/熔断 blockHandler
     * 当请求被限流或熔断时执行此方法
     */
    public Long createOrderBlockHandler(OrderRequestDTO request, BlockException e) {
        log.warn("Order creation blocked by Sentinel: {}", e.getClass().getSimpleName());
        throw new RuntimeException(ResultCode.REQUEST_LIMITED.getMsg());
    }

    /**
     * Sentinel 降级 fallback
     * 当远程调用异常比例高时触发降级
     */
    public Long createOrderFallback(OrderRequestDTO request, Throwable t) {
        log.warn("Order creation fallback triggered: {}", t.getMessage());
        throw new RuntimeException(ResultCode.SERVICE_DEGRADED.getMsg());
    }
}