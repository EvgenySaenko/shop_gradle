package ru.geekbrains.shop.persistence.entities;

import lombok.*;
import ru.geekbrains.shop.persistence.entities.utils.PersistableEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders_items")
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends PersistableEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;


    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(Product product){
        this.product = product;
        this.quantity = 1;
        this.price = new BigDecimal(0).add(BigDecimal.valueOf(product.getPrice()));
    }

    public void increment() {
        this.quantity++;
        this.price = new BigDecimal(quantity * product.getPrice().doubleValue());
    }

    public void decrement() {
        this.quantity--;
        this.price = new BigDecimal(quantity * product.getPrice().doubleValue());
    }

    public boolean isEmpty(){
        return quantity == 0;
    }

}

