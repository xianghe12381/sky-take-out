package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     *
     * @param dish
     */
    void insert(Dish dish);

    // 在 DishMapper 接口中添加

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 动态条件查询菜品
     *
     * @param dish
     * @return
     */
    List<com.sky.vo.DishVO> list(Dish dish);

    // 在 DishMapper.java 中添加
    /**
     * 根据主键列表批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);


    // DishMapper.java
    List<Dish> getByIds(List<Long> ids);

}
