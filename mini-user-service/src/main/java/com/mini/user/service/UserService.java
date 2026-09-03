package com.mini.user.service;

import com.mini.common.dto.UserDTO;
import com.mini.user.entity.User;

public interface UserService {

    UserDTO getUserById(Long userId);

    void deductBalance(Long userId, Integer amount);
}