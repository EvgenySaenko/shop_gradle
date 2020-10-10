package ru.geekbrains.shop.services;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import ru.geekbrains.shop.dto.ProductDto;
import ru.geekbrains.shop.dto.ReviewDto;
import ru.geekbrains.shop.persistence.entities.Image;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.persistence.entities.Review;
import ru.geekbrains.shop.persistence.entities.Shopuser;
import ru.geekbrains.shop.persistence.entities.enums.Role;
import ru.geekbrains.shop.persistence.repositories.ReviewRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Api("Выполняет бизнес логику с коментариями")
public class ReviewService {


    private final ReviewRepository reviewRepository;
    private final AmqpTemplate amqpTemplate;

    @ApiOperation(value = "Достает список комментариев к данному продукту", response = List.class)
    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    @ApiOperation(value = "Достает список комментариев оставленных данным пользователем", response = List.class)
    public List<Review> getReviewsByShopuser(Shopuser shopuser) {
        return reviewRepository.findByShopuser(shopuser);
    }

    @ApiOperation(value = "Сохраняет комментарий в базе данных")
    public void save(Review review) {
        reviewRepository.save(review);
    }

    @ApiOperation(value = "Сохраняет комментарий в базе данных")
    public void save(ReviewDto reviewDto, Image image, Optional<Product> productOptional, Optional<Shopuser> shopuserOptional) {
        Review review = Review.builder()
                .commentary(reviewDto.getCommentary())
                .shopuser(shopuserOptional.get())
                .product(productOptional.get())
                .approved(shopuserOptional.get().equals(Role.ROLE_ADMIN))
                .image(image)
                .build();
        amqpTemplate.convertAndSend("super-shop.exchange","super.shop","User has left review");
        reviewRepository.save(review);
    }
    @ApiOperation(value = "Модерация комментариев",response = UUID.class)
    public UUID moderate(UUID id, String option){
        Optional<Review> reviewOptional = reviewRepository.findById(id);//ищем комент по id
        if (reviewOptional.isPresent()){//если такой есть
            Review review = reviewOptional.get();//делаем ревьюшку
            review.setApproved(option.equals("approve"));//устанавливаем аппрув-поддтвердили или нет отзыв
            save(review);//сохраняем ревьюшку
            return review.getProduct().getId();
        }else {
            return null;
        }
    }
}