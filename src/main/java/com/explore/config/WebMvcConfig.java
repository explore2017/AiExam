package com.explore.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TLoginInterceptor tLoginInterceptor;
    @Autowired
    private MLoginInterceptor mLoginInterceptor;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns("/**") 表示拦截所有的请求，
        // excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
        registry.addInterceptor(tLoginInterceptor).
                addPathPatterns("/question","/subject/**","/teacher/**","/paper/**","/class/**","/manage/**","/exam/**").
                excludePathPatterns("/teacher/login","/manage/login","/exam/batch/enroll/{batch_id}","/exam/batch/details/{studentId}","/exam/batch/{id}/sign","/exam/batch/{id}/check","/exam/batch/{id}/start","/exam/batch/{id}/monitor","/exam/batch/{id}/submit");
        registry.addInterceptor(mLoginInterceptor).
                addPathPatterns("/manage/**").
                excludePathPatterns("/teacher/login","/manage/login","/manage/reviseStudent","/manage/insertStudent","/manage/deleteStudent/","/manage/students");
    }


}
