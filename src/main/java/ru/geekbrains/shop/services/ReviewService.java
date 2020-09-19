package ru.geekbrains.shop.services;

import lombok.RequiredArgsConstructor;

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
public class ReviewService {


    private final ReviewRepository reviewRepository;

    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public List<Review> getReviewsByShopuser(Shopuser shopuser) {
        return reviewRepository.findByShopuser(shopuser);
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public void save(ReviewDto reviewDto, Image image, Optional<Product> productOptional, Optional<Shopuser> shopuserOptional) {
        Review review = Review.builder()
                .commentary(reviewDto.getCommentary())
                .shopuser(shopuserOptional.get())
                .product(productOptional.get())
                .approved(shopuserOptional.get().equals(Role.ROLE_ADMIN))
                .image(image)
                .build();
        reviewRepository.save(review);
    }

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