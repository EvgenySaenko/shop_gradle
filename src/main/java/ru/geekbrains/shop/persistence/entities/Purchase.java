package ru.geekbrains.shop.persistence.entities;


import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.geekbrains.shop.persistence.entities.utils.PersistableEntity;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Purchase extends PersistableEntity {

    private Double price;

    private String address;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<CartRecord> cartRecords;

    @ManyToOne
    @JoinColumn(name = "shopuser")
    private Shopuser shopuser;

}