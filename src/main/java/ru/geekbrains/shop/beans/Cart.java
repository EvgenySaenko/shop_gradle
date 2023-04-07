package ru.geekbrains.shop.beans;

import lombok.Data;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ru.geekbrains.paymentservice.Payment;
import ru.geekbrains.shop.persistence.entities.OrderItem;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.services.soap.PaymentService;

import javax.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {

    private List<OrderItem> items;
    private List<Payment> payments;
    private BigDecimal price;

    private final PaymentService paymentService;


    @PostConstruct
    public void init() {
        this.items = new ArrayList<>();
        payments = paymentService.getPayments("Russia");
    }

    public void clear(){
        items.clear();
        recalculate();
    }

    public void add(Product product) {
        for (OrderItem i : items) {
            if (i.getProduct().getId().equals(product.getId())) {
                i.increment();
                recalculate();
                return;
            }
        }
        items.add(new OrderItem(product));
        recalculate();
    }

    public void decrement(Product product) {
        for (OrderItem i : items) {
            if (i.getProduct().getId().equals(product.getId())) {
                i.decrement();
                if (i.isEmpty()) {
                    items.remove(i);
                }
                recalculate();
                return;
            }
        }
        items.add(new OrderItem(product));
        recalculate();
    }

    public void removeByProductId(UUID productId){
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getId().equals(productId)) {
                items.remove(i);
                recalculate();
                return;
            }
        }
    }

    public void recalculate() {
        price = new BigDecimal(0.0);
        for (OrderItem i : items) {
            price = price.add(i.getPrice());
        }
    }
}