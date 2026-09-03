package com.mini.user.service.impl;

import com.mini.common.dto.UserDTO;
import com.mini.common.result.ResultCode;
import com.mini.user.entity.User;
import com.mini.user.mapper.UserMapper;
import com.mini.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(ResultCode.USER_NOT_FOUND.getMsg());
        }
        return toDTO(user);
    }

    @Override
    public void deductBalance(Long userId, Integer amount) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(ResultCode.USER_NOT_FOUND.getMsg());
        }
        if (user.getBalance() < amount) {
            throw new RuntimeException(ResultCode.INSUFFICIENT_BALANCE.getMsg());
        }
        int rows = userMapper.deductBalance(userId, amount);
        if (rows <= 0) {
            throw new RuntimeException(ResultCode.BALANCE_DEDUCT_FAILED.getMsg());
        }
        log.info("User {} balance deducted: {}, remaining: {}", userId, amount, user.getBalance() - amount);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setBalance(user.getBalance());
        return dto;
    }
}