package com.mini.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mini.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("UPDATE t_user SET balance = balance - #{amount} WHERE id = #{userId} AND balance >= #{amount}")
    int deductBalance(Long userId, Integer amount);
}