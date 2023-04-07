package ru.geekbrains.shop.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.shop.dto.ReviewDto;
import ru.geekbrains.shop.persistence.entities.Image;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.persistence.entities.Review;
import ru.geekbrains.shop.persistence.entities.Shopuser;
import ru.geekbrains.shop.services.ImageService;
import ru.geekbrains.shop.services.ProductService;
import ru.geekbrains.shop.services.ReviewService;
import ru.geekbrains.shop.services.ShopuserService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ShopuserService shopuserService;
    private final ReviewService reviewService;
    private final ImageService imageService;
    private final ProductService productService;


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//доступ только для админа
    public String moderateReview(@PathVariable UUID id, @RequestParam String option){
        return "redirect:/products/" + reviewService.moderate(id,option);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> getAllReviews(){
        return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Добавляет комментарий к товару", response = String.class)
    public String addReview(@RequestParam("image") MultipartFile image, ReviewDto reviewDto, HttpSession httpSession, Principal principal) throws IOException {

        //чтобы проверить совпала ли каптча
        if (httpSession.getAttribute("captchaCode").equals(reviewDto.getCaptchaCode())){
            System.out.println("TRUE");
        }else {
            System.out.println("FALSE");
        }
        Optional<Product> productOptional = productService.getOneById(reviewDto.getProductId());
        Optional<Shopuser> shopuserOptional = shopuserService.findByPhone(principal.getName());

        //если существует продукт и юзер, то формируем ревью
        if (productOptional.isPresent() && shopuserOptional.isPresent()) {
            Image img = imageService.uploadImage(image, imageService.generateNameImage(reviewDto,productOptional));
            reviewService.save(reviewDto,img,productOptional,shopuserOptional);
            return "redirect:/products/" + productOptional.get().getId();
        }
        return "redirect:/";
    }



}
