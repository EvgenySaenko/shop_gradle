package ru.geekbrains.shop.persistence.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import ru.geekbrains.shop.persistence.entities.utils.PersistableEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "Класс описывающий сущность комментарий(review)")
public class Review extends PersistableEntity {

    @ApiModelProperty(required = true, value = "Содержание комментария")
    private String commentary;

    @ManyToOne
    @JoinColumn(name = "shopuser")
    @ApiModelProperty(required = true, value = "Пользователь оставивший комментарий")
    private Shopuser shopuser;

    @ManyToOne
    @JoinColumn(name = "product")
    @ApiModelProperty(required = true, value = "Продукт к которому оставлен комментарий")
    private Product product;

    @OneToOne
    @JoinColumn(name = "image")
    @ApiModelProperty(required = true, value = "Изображение к комментарию")
    private Image image;

    @ApiModelProperty(required = true, value = "Статус комментария(одобрен/отклонен)")
    private boolean approved;

}