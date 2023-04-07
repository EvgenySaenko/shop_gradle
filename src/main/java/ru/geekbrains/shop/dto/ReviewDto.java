package ru.geekbrains.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.shop.persistence.entities.Image;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.persistence.entities.Shopuser;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private UUID id;
    private String captchaCode;
    private String commentary;
    private Shopuser shopuser;
    private UUID productId;
}