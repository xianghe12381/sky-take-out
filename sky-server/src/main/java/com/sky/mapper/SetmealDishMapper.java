package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    // SetmealDishMapper.java
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
// 或者直接返回 count 也可以，看你喜欢怎么处理
}
