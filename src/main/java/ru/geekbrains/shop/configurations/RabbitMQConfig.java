//package ru.geekbrains.shop.configurations;
//
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//
//import org.springframework.amqp.core.Queue;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class RabbitMQConfig {
//
//    @Value("${geekshop.rabbitmq.exchange}")
//    private String EXCHANGE;
//
//    @Value("${geekshop.rabbitmq.routingkey}")
//    private String ROUTING_KEY;
//
//    @Value("${geekshop.rabbitmq.queue}")
//    private String QUEUE;
//
//    @Bean
//    public DirectExchange exchange() {
//        return new DirectExchange(EXCHANGE);
//    }
//
//    @Bean
//    Queue queue() {//durable- хранится в очереди пока его кто-нибудь не вычитает
//        return new Queue(QUEUE, true, false, false);
//    }
//
//    @Bean
//    Binding binding() {//связываем exchange c queue с помощью ключа(ROUTING_KEY)
//        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY);
//    }
//
//}
