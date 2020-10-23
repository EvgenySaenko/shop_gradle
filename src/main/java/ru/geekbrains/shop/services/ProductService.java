package ru.geekbrains.shop.services;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import ru.geekbrains.shop.dto.ProductDto;
import ru.geekbrains.shop.persistence.entities.Image;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.shop.persistence.repositories.ProductRepository;
import ru.geekbrains.shop.services.notification.MailService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Api("Выполняет бизнес логику с товарами")
public class ProductService {

    private final ProductRepository productRepository;

    @ApiOperation(value = "Достает товар по его id", response = Optional.class)
    public Optional getOneById(UUID id) {
        return productRepository.findById(id);
    }
    //если мы не указали категорию - будет выводится просто список всех продуктов, если указали будет осуществлен поиск по категории

    @ApiOperation(value = "Достает список товаров, если указана категория - выводит списко товаров этой категории", response = List.class)
    public List<Product> getAll(Integer category) {//вызвали массив енумов .values()[] и внутрь передали категорию
        return category == null ? productRepository.findAll() : productRepository.findAllByCategory(ProductCategory.values()[category]);
    }

    @ApiOperation(value = "Добавляет и сохраняет товар в базу данных с картинкой", response = String.class)
    public String save(ProductDto productDto, Image image) {

        Product product = Product.builder()
            .added(new Date())
            .title(productDto.getTitle())
            .available(productDto.isAvailable())
            .category(productDto.getCategory())
            .price(productDto.getPrice())
            .description(productDto.getDescription())
            .image(image)
        .build();

        productRepository.save(product);

        log.info("New Product has been succesfully added! {}", product);
        return "redirect:/";
    }

}