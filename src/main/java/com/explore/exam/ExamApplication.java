package com.explore.exam;

import com.explore.service.Impl.ManageServicempl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@ComponentScan(basePackages = {"com.explore"})
@MapperScan(basePackages = {"com.explore.dao"})
@EnableScheduling
public class ExamApplication {

    public static void main(String[] args) { SpringApplication.run(ExamApplication.class, args);
    }

}

