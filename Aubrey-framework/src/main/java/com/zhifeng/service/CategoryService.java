package com.zhifeng.service;

import com.zhifeng.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhifeng.entity.dto.AddCategoryDto;
import com.zhifeng.entity.dto.CategoryListDto;
import com.zhifeng.entity.dto.UpdateCategoryDto;
import com.zhifeng.util.ResponseResult;

/**
 *
 */
public interface CategoryService extends IService<Category> {
    /**
     * 返回查询文章分类列表,包装信息在ResponseResult中
     * @return
     */
    ResponseResult getCategoryList();
    //在发布博文界面:查询所有文章分类(CategoryVo)数据
    ResponseResult listAllCategory();
    //访问分类信息列表数据:分页显示(响应到前端)
    ResponseResult pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto);
    //实现新增分类信息接口
    ResponseResult addCategory(AddCategoryDto addCategoryDto);
    //根据id查询分类
    ResponseResult getCategoryById(Long id);
    //更新分类信息
    ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto);
    //根据id删除分类信息
    ResponseResult deleteCategory(Long id);
}
