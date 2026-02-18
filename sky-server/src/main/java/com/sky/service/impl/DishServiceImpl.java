package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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

    // 在 DishServiceImpl 类中添加
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        // 1. 开始分页 (PageHelper 会自动拦截下一条 SQL 添加 limit)
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        // 2. 调用 Mapper (这里返回的是 Page<DishVO>，PageHelper 的泛型)
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        // 3. 封装结果
        return new PageResult(page.getTotal(), page.getResult());

    }

    /**
     * 条件查询菜品和口味
     *
     * @param dish
     * @return
     */
    public List<com.sky.vo.DishVO> listWithFlavor(Dish dish) {
        // 1. 先查出符合条件的菜品（比如川菜下的所有菜）
        List<com.sky.vo.DishVO> dishVOList = dishMapper.list(dish);

        // 2. 遍历每一个菜品，去查它对应的口味
        for (com.sky.vo.DishVO dishVO : dishVOList) {
            // 根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(dishVO.getId());
            // 把查到的口味塞进菜品对象里
            dishVO.setFlavors(flavors);
        }

        return dishVOList;
    }
}