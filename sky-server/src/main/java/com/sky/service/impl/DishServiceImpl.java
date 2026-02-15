package com.sky.service.impl;


import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Transactional // 涉及两张表操作，必须开启事务
    public void saveWithFlavor(DishDTO dishDTO) {
        // 1. DTO -> Entity (Dish)
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 2. 向菜品表插入1条数据
        // 此时，MyBatis 会拦截 SQL 返回的主键，并自动填入 dish 对象的 id 属性中
        dishMapper.insert(dish);

        /*前面的代码是插入菜品信息的，操控dish表*/


        // 3. 获取生成的菜品主键 ID
        Long dishId = dish.getId();

        // 4. 处理口味列表
        List<DishFlavor> flavors = dishDTO.getFlavors();//这里的getFlavors()是get和set方法
        if (flavors != null && flavors.size() > 0) {
            // 给每个口味对象填上“爸爸的身份证号”
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId); //手动添加逻辑外键
            });
            // 5. 向口味表批量插入数据
            dishFlavorMapper.insertBatch(flavors);
        }
        /*这里的代码是操控dish_flavor表的*/
    }
}