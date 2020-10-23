package ru.geekbrains.shop.persistence.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cascade;
import ru.geekbrains.shop.beans.Cart;
import ru.geekbrains.shop.persistence.entities.utils.PersistableEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends PersistableEntity {


    @ManyToOne
    @JoinColumn(name = "user_id")
    private Shopuser user;

    @OneToMany(mappedBy = "order")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<OrderItem>items;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "address")
    private String address;

    public Order(Shopuser user, Cart cart, String phone, String address) {
        this.user = user;
        this.phone = phone;
        this.address = address;
        this.items = new ArrayList<>();
        for (OrderItem oi : cart.getItems()){//обходим все айтемы из корзины
            //ордер айтем ты ссылаешься на меня(на этот ордер) привязываем по внешнему ключу к данному ордеру
            oi.setOrder(this);
            this.items.add(oi);//добавляем в айтемы этот ордер айтем()
        }
        //копируем стоимость всей корзины в наш заказ
        this.price = new BigDecimal(cart.getPrice().doubleValue());
        cart.clear();
    }



}
