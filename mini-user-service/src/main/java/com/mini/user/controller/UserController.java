package com.mini.user.controller;

import com.mini.common.dto.DeductBalanceDTO;
import com.mini.common.dto.UserDTO;
import com.mini.common.result.Result;
import com.mini.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public Result<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return Result.success(user);
    }

    @PostMapping("/deduct")
    public Result<Void> deductBalance(@RequestBody DeductBalanceDTO dto) {
        userService.deductBalance(dto.getUserId(), dto.getAmount());
        return Result.success();
    }
}