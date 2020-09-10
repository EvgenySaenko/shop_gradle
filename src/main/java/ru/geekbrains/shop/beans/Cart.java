package ru.geekbrains.shop.beans;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.geekbrains.shop.persistence.entities.CartRecord;
import ru.geekbrains.shop.persistence.entities.Product;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Component
//используем scope session
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {

    private List<CartRecord> cartRecords;
    private Double price;

    @PostConstruct
    public void init() {//инициализируем пустую корзину при создании бина
        cartRecords = new ArrayList<>();
    }

    public void clear() {//чистим список с покупками
        cartRecords.clear();
        recalculatePrice();
    }

    public void add(Product product) {
        for (CartRecord cartRecord : cartRecords) {
            if (cartRecord.getProduct().getId().equals(product.getId())) {//если такой продукт есть
                cartRecord.setQuantity(cartRecord.getQuantity() + 1);//увеличиваем его на 1
                cartRecord.setPrice(cartRecord.getQuantity() * cartRecord.getProduct().getPrice());//считаем общую сумму
                recalculatePrice();//пересчет цены продуктов в корзине
                return;
            }
        }
        //если нет такого товара просто добавляем его в корзину
        cartRecords.add(new CartRecord(product));
        recalculatePrice();// TODO: 10.09.2020 при добавлении в список не делали пересчет-исправил
    }

    public void removeByProductId(UUID productId) {
        for (int i = 0; i < cartRecords.size(); i++) {
            if (cartRecords.get(i).getProduct().getId().equals(productId)) {
                cartRecords.remove(i);
                recalculatePrice();//пересчет цены продуктов в корзине
                return;
            }
        }
    }
    //при каждой манипуляции с корзиной - пересчитывает цену продуктов в корзине
    private void recalculatePrice() {
        price = 0.0;
        cartRecords.forEach(cartRecord -> price = price + cartRecord.getPrice());
    }

}