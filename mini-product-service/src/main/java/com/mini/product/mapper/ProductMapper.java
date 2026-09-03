package com.mini.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mini.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Update("UPDATE t_product SET stock = stock - #{count} WHERE id = #{productId} AND stock >= #{count}")
    int deductStock(Long productId, Integer count);
}