package com.zhifeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhifeng.constant.SystemConstants;
import com.zhifeng.entity.Article;
import com.zhifeng.entity.Category;
import com.zhifeng.entity.dto.AddCategoryDto;
import com.zhifeng.entity.dto.CategoryListDto;
import com.zhifeng.entity.dto.UpdateCategoryDto;
import com.zhifeng.entity.vo.CategoryVo;
import com.zhifeng.entity.vo.PageVo;
import com.zhifeng.entity.vo.UpdateCategoryVo;
import com.zhifeng.enums.AppHttpCodeEnum;
import com.zhifeng.service.ArticleService;
import com.zhifeng.service.CategoryService;
import com.zhifeng.mapper.CategoryMapper;
import com.zhifeng.util.BeanCopyUtils;
import com.zhifeng.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 实现文章分类表相关功能
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService, SystemConstants {
    @Autowired  //封装文章实体类业务组件
    private ArticleService articleService;
    /**
     * 返回文章分类表信息(即点击首页分类按钮,显示java、PHP、C++等分类列表)
     *   考虑条件:1.要求只展示有发布正式文章(非草稿status=1)的分类,也就是当前分类下具有发布的正式文章
     *                  即文章表中每个文章具有category_id字段对应文章分类表的主键Id
     *          2.是正常状态的分类
     *                  文章所属分类表中,具有字段status，0表示正常,1表示禁用
     *          不使用连表查询得到
     *          3.查询过程如下:
     *              3.1 因此可以先得到所有已发布文章构成的集合articlelist
     *              3.2 通过articlelist集合得到所有文章所在分类,并进行去重,最后得到不重复的文章分类表的主键id集合
     *              3.3 最后通过集合categoryIds查询得到所有符合要求的文章分类集合categories
     *              3.4 封装categories<Category>为集合categoryVos<CategoryVo>类进行返回
     *
     * @return
     */
    @Override
    public ResponseResult getCategoryList() {
        //1.先得到所有已发布文章构成的集合articlelist
            //1.1使用MybatisPlus提供的LambdaQueryWrapper类进行查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);//封装条件: 表示所有已发布的文章
        List<Article> articleList = articleService.list(queryWrapper);//得到所有已发布文章构成的集合articlelist

        //2.通过articlelist集合得到所有文章所在分类,并进行去重,最后得到不重复的文章分类表的主键id集合categoryIds
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());//得到set集合可以自动进行去重
        //3.通过集合categoryIds查询得到所有符合要求的文章分类集合categories(所在分类正常,即status=1)
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> category.getStatus().equals(ARTICLE_STATUS_NORMAL+""))//0表示正常分类
                .collect(Collectors.toList());
        //4.封装categories<Category>为集合categoryVos<CategoryVo>类进行返回
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 在发布博文界面:查询所有文章分类(CategoryVo)数据
     *      文章类型需要正常状态status = 0
     * @return
     */
    @Override
    public ResponseResult listAllCategory() {
        //1.查询所有状态正常的文章
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Category> list = list(queryWrapper);
        //2.封装为CategoryVo对象进行返回
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 访问分类信息列表数据:分页显示(响应到前端)
     * @param pageNum 每页显示数据大小
     * @param pageSize 当前页
     * @param categoryListDto   封装分类查询的分类名name和状态status
     * @return
     */
    @Override
    public ResponseResult pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto) {
        //1.如果categoryListDto不为空(存在查询条件name或者status),则根据条件进行分页查询;否则显示所有分类信息
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(categoryListDto.getName()),Category::getName,categoryListDto.getName());//如果存在,则按照name进行查询
        queryWrapper.eq(StringUtils.hasText(categoryListDto.getStatus()),Category::getStatus,categoryListDto.getStatus());//如果存在,则按照status进行查询
        //2.封装MP的分页对象Page
        Page<Category> page = new Page<>(pageNum,pageSize);
        //等价于
//        page.setCurrent(pageNum);
//        page.setSize(pageSize);
        page(page,queryWrapper);
        //3.封装为分页对象PageVo返回
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 实现新增分类信息接口
     * @param addCategoryDto    新增分类对象
     * @return
     */
    @Override
    public ResponseResult addCategory(AddCategoryDto addCategoryDto) {
        //1.转换
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    /**
     * 根据id查询分类
     *      返回UpdateCategoryVo实体类,封装响应信息
     * @param id
     * @return
     */
    @Override
    public ResponseResult getCategoryById(Long id) {
        //1.根据id查询分类
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.ID_NOT_NULL);
        }
        Category category = getById(id);
        //2.封装为UpdateCategoryVo实体类进行返回
        UpdateCategoryVo categoryVo = BeanCopyUtils.copyBean(category, UpdateCategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    /**
     * 更新分类信息
     * @param updateCategoryDto
     * @return
     */
    @Override
    public ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto) {
        //1.根据id进行修改
        if(updateCategoryDto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.ID_NOT_NULL);
        }
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId,updateCategoryDto.getId());
        Category category = BeanCopyUtils.copyBean(updateCategoryDto, Category.class);
        update(category,queryWrapper);
        return ResponseResult.okResult();
    }

    /**
     * 根据id删除分类信息
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteCategory(Long id) {
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.ID_NOT_NULL);
        }
        removeById(id);
        return ResponseResult.okResult();
    }
}




