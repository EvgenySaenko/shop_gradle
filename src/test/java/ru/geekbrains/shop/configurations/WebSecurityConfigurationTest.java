//package ru.geekbrains.shop.configurations;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//@Order(1)//аннотация исполнения- порядок исполнения
//@Configuration
//public class WebSecurityConfigurationTest  extends WebSecurityConfigurerAdapter {
//
//    //этот класс для того чтобы, если нам нужно провести тесты а там нужны Права Доступа
//    //то мы в этом классе ставим определенные эндпоинты которые должны игнорится
//    //нашей безопасностью
//
//    @Override
//    public void configure(WebSecurity web)  {
//        web.ignoring().antMatchers("/reviews");
//    }
//}
