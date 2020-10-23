package ru.geekbrains.shop.persistence.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.shop.persistence.entities.Order;

@Repository
public interface OrdersRepository extends CrudRepository<Order,Long> {
}
