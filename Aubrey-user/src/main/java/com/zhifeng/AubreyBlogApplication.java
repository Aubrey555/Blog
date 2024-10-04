package com.zhifeng;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhifeng.entity.User;
import com.zhifeng.mapper.UserMapper;
import org.junit.jupiter.api.DynamicTest;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;

/**
 * @Author: 屈志峰
 * @Version:1.0
 * @Date:2022/12/14 20:29
 * @Description:
 */@MapperScan("com.zhifeng.mapper")   //扫描mapper接口所在包
@SpringBootApplication      //该注解表示SpringBoot的启动类
@EnableScheduling   //该注解表示开启定时任务功能
@EnableSwagger2 //该注解表示启动swagger功能:生成开发的接口文档
public class AubreyBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(AubreyBlogApplication.class,args);
    }
}

