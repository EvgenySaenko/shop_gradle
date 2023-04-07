package ru.geekbrains.shop.services;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import ru.geekbrains.shop.persistence.entities.Order;
import ru.geekbrains.shop.persistence.repositories.OrdersRepository;
import ru.geekbrains.shop.services.notification.MailService;

@Slf4j
@Service
@RequiredArgsConstructor
@Api("Выполняет бизнес логику с заказами")
public class OrdersService {

    private final MailService mailService;
    private OrdersRepository ordersRepository;


    @Autowired
    public void setOrdersRepository(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public Order saveOrder(Order order) {
        //почему мы тут не сохраняем ордер айтемы?
        //потому что в Order у нас включены каскадные операции, и сохраняя ордер мы сохраним ордер айтемы
        Context context = new Context();//создаем таймлифовский контекст просто для примера
        context.setVariable("order_price",order.getPrice());
        context.setVariable("order_phone",order.getPhone());
        context.setVariable("order_address",order.getAddress());
        mailService.send("razumserdca777@gmail.com","Подтверждение вашего заказа",context);
        log.info("New order successfully created! {}", order.getId());
        return ordersRepository.save(order);
    }
}
