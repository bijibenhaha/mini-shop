package com.mini.order.feign;

import com.mini.common.dto.DeductBalanceDTO;
import com.mini.common.dto.UserDTO;
import com.mini.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mini-user-service", path = "/api/user")
public interface UserFeignClient {

    @GetMapping("/{userId}")
    Result<UserDTO> getUser(@PathVariable Long userId);

    @PostMapping("/deduct")
    Result<Void> deductBalance(@RequestBody DeductBalanceDTO dto);
}