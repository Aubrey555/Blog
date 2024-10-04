package com.zhifeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhifeng.constant.SystemConstants;
import com.zhifeng.entity.Article;
import com.zhifeng.entity.ArticleTag;
import com.zhifeng.entity.Category;
import com.zhifeng.entity.dto.AddArticleDto;
import com.zhifeng.entity.dto.ArticleListDto;
import com.zhifeng.entity.vo.ArticleDetailVo;
import com.zhifeng.entity.vo.ArticleListVo;
import com.zhifeng.entity.vo.HotArticleVo;
import com.zhifeng.entity.vo.PageVo;
import com.zhifeng.enums.AppHttpCodeEnum;
import com.zhifeng.service.ArticleService;
import com.zhifeng.mapper.ArticleMapper;
import com.zhifeng.service.ArticleTagService;
import com.zhifeng.service.CategoryService;
import com.zhifeng.util.BeanCopyUtils;
import com.zhifeng.util.RedisCache;
import com.zhifeng.util.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 操作Article文章表的Service层组件
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService, SystemConstants {
    @Autowired  //文章分类业务:需要通过文章所在类id得到文章分类名
    private CategoryService categoryService;
    @Autowired  //自定义实现类,封装redis中对数据的一般操作
    private RedisCache redisCache;
    @Autowired  //文章标签表的实现接口
    private ArticleTagService articleTagService;
    /**
     * 得到访问量最高的前10篇文章
     * @return      热门文章列表:封装成ResponeseResult对象返回
     */
    @Override
    public ResponseResult hotArticleList() {
        //1.使用MybatisPlus提供的LambdaQueryWrapper类进行查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //2.查询条件:
            // 必须是正式文章,非草稿(status=0)
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
            // 按照浏览量进行排序:ViewCount(从高到低,降序)
        queryWrapper.orderByDesc(Article::getViewCount);
            // 只查询前10条
        //封装分页对象:,第一页,  前10条数据
        Page<Article> page = new Page<>(1,10);
        page(page,queryWrapper);//传入分页对象和查询条件,封装查询结果
        List<Article> articleList = page.getRecords();//得到查询得到的列表数据
        //3. 封装VO实体类集合,返回到前端
        ArrayList<HotArticleVo> articleVos = new ArrayList<>();
            // 利用bean拷贝封装Article对象为HotArticleVo
        for (Article article:articleList){
            HotArticleVo vo = new HotArticleVo();
            //利用工具类BeanUtils完成Bean拷贝
            BeanUtils.copyProperties(article,vo);
            articleVos.add(vo);
        }
        //返回查询得到的热门文章列表:VO对象的article
        return ResponseResult.okResult(articleVos);
    }

    /**
     * 实现通过指定分页信息,查询文章列表
     *      要求：①只能查询正式发布(status=0表示正式发布)的文章
     *           ②置顶的文章(文章的is_top字段=1表示置顶)要显示在最前面,否则按照创建时间进行排序。
     * @param pageNum   查询的当前页码
     * @param pageSize  当前页面显示数据条数  通过此两条参数封装page对象
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //1.使用Lambda封装查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //2.封装查询条件
            //2.1 如果categoryId存在,则查询对应分类的文章集合;不存在则查询所有文章(动态sql条件)
                //即如果前端传入分类id存在(condition==true),则通过分类id进行查询,如果不存在,则以下条件不执行
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,
                    Article::getCategoryId,categoryId);
            //2.2 查询正式发布的文章(status=0)
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
            //2.3 置顶文章显示前列:对is_top字段进行降序排序,一样时按照创建时间降序(最新的时间在前面)
        queryWrapper.orderByDesc(Article::getIsTop).orderByDesc(Article::getCreateTime);

        //3.对条件进行分页查询:传入查询当前页码  当前页数据总数
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);

        //4.ArticleListVo类中属性categoryName表示当前文章所在类的类名:Article中存在此属性,但表中没有该字段(只有所在类id)
            //因此查询sg_category文章分类表,对Article中的属性categoryName进行赋值
        List<Article> articles = page.getRecords();
            //1. 使用lambda表达式对Article:categoryName进行封装赋值
        articles.stream()
                //此时article.setCategoryName()方法返回值为Article才可成功,即使用@Accessors注解标注在Article实体类上
                //@Accessors(chain = true)    //此注解将当前实体类Article的所有set方法返回值设置为实体类对象Article
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
            //2. 使用lambda表达式对Article:categoryName进行封装赋值
//        articles.stream()
//                .map(new Function<Article, Article>() {
//                    @Override
//                    public Article apply(Article article) {
//                        //实现:通过Article:categoryId ->  获得Article:categoryName
//                        Category category = categoryService.getById(article.getCategoryId());
//                        article.setCategoryName(category.getName());
//                        return article;
//                    }
//                })
//                .collect(Collectors.toList());
            //3.遍历articles对Article:categoryName进行封装赋值
//        for (Article article:articles){
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        //5.封装查询结果(集合)中的所有Article对象为VO对象(文章实体类封装的VO类:即需要响应给前端的部分数据所封装)
        List<ArticleListVo> listVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
            //前端需要数据封装到PageVo中:所有查询得到的Article对象组成的集合,以及查询得到的总记录数
        PageVo pageVo = new PageVo(listVos, page.getTotal());

        //6.返回封装的分页VO
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 通过id查询文章详情信息:封装为ArticleDetailVo
     *      更新后:从redis中获取id对应的博客的浏览量
     * @param id        博客id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //1.通过id查询文章信息
        Article article = getById(id);
            //key = article:viewCount键中存储map集合中的数据,从redis中获取当前文章的viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());//更新当前博客的访问量

        //2.将得到的Artcile转换为ArticleDetailVo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //3.并根据ArticleDetailVo.categoryId分类id查询对应的分类名
            //得到文章所属的分类对象
        Category category = categoryService.getById(article.getCategoryId());
            //通过分类对象获得文章所属类型
        if(category != null){//文章所属类存在
            //为文章详情articleDetailVo实体类赋值:目录类名称
            articleDetailVo.setCategoryName(category.getName());
        }
        //4.返回文章详情信息
        return ResponseResult.okResult(articleDetailVo);
    }

    /**
     * 实现根据给定的博客id更新redis中当前博客的访问次数
     * @param id    博客id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        //每当访问该请求,则博客浏览量+1
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    /**
     * 后端工程:实现发布博文功能
     * @param addArticleDto 前端传输的博文信息
     * @return
     */
    @Override
    @Transactional  //保证原子性
    public ResponseResult add(AddArticleDto addArticleDto) {
        //1.添加博客,发布博文
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);
        //2.添加博客id 与 标签id对应关联(即sg_article_tag表相关数据)
        List<ArticleTag> articleTags = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    /**
     * 实现后台项目查询文章列表功能
     * @param pageNum  当前页码
     * @param pageSize  每页显示大小
     * @param articleListDto   封装传入数据:文章标题和文章摘要
     * @return
     */
    @Override
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        //1.如果articleListDto不为空,则根据文章标题和文章摘要进行搜索;如果为空,则搜索所有文章列表
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle,articleListDto.getTitle());
        queryWrapper.eq(StringUtils.hasText(articleListDto.getSummary()),Article::getSummary,articleListDto.getSummary());
        //2.创建MP的page对象,进行分页显示
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
        //3.封装分页对象返回
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 在文章管理界面,点击修改,跳转到查询文章详情界面,通过文章id,显示博文Article
     * @param id
     * @return
     */
    @Override
    public ResponseResult queryArticle(Integer id) {
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.ID_NOT_NULL);
        }
        Article article = getById(id);
        return ResponseResult.okResult(article);
    }

    /**
     * 通过给定实体,更新文章
     * @param article
     * @return
     */
    @Override
    public ResponseResult updateArticle(Article article) {
        if(article == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.ARTICLE_NOT_NULL);
        }
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,article.getId());

        boolean success = update(article,queryWrapper);
        return success == true ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 实现根据id删除文章(逻辑删除)
     * @param id
     */
    @Override
    public void deleteArticle(Integer id) {
        if(id == null){
            return;
        }
        removeById(id);
    }
}




