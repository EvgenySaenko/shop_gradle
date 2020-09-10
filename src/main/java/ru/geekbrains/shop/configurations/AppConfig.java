package ru.geekbrains.shop.configurations;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc//укажем дирректорию где искать изображения
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry//это папка с картинками, ее указываем в thymeleaf - все вместо ** это дальнейший адрес
                .addResourceHandler("/images/**")
                .addResourceLocations("file:F:/Spring/uploads/images/");//это где находится папка images
    }

}
