package ru.geekbrains.shop.controllers;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.shop.beans.Cart;
import ru.geekbrains.shop.persistence.entities.Order;
import ru.geekbrains.shop.persistence.entities.Shopuser;
import ru.geekbrains.shop.services.OrdersService;
import ru.geekbrains.shop.services.ShopuserService;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final ShopuserService usersService;
    private OrdersService ordersService;
    private Cart cart;

    @GetMapping("/create")
    public String createOrder(Principal principal, Model model) {
        Shopuser user = usersService.findByPhone(principal.getName()).get();
        model.addAttribute("user", user);
        return "order_info";
    }

    @PostMapping("/confirm")
    public String confirmOrder(Principal principal, @RequestParam String address, @RequestParam String phone, Model model) {
        Shopuser user = usersService.findByPhone(principal.getName()).get();//вытаскиваем имя юзера(а имя юзера у нас телефон)
        Order order = new Order(user, cart, phone, address);//собираем заказ
        order = ordersService.saveOrder(order);
        model.addAttribute("order",order);
        //после этого заказ сормирован, а корзина стала пустой
        return "order_results";
    }

}
