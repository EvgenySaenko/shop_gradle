package ru.geekbrains.shop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import ru.geekbrains.shop.dto.ProductDto;
import ru.geekbrains.shop.dto.ReviewDto;
import ru.geekbrains.shop.persistence.entities.Image;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.persistence.entities.Review;
import ru.geekbrains.shop.persistence.entities.Shopuser;
import ru.geekbrains.shop.persistence.entities.enums.Role;
import ru.geekbrains.shop.services.ImageService;
import ru.geekbrains.shop.services.ProductService;
import ru.geekbrains.shop.services.ReviewService;
import ru.geekbrains.shop.services.ShopuserService;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ImageService imageService;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final ShopuserService shopuserService;

    @GetMapping("/")
    public String allProducts(Model model, @RequestParam(required = false) Integer category) {
        model.addAttribute("products", productService.getAll(category));
        return "products";
    }



    @PostMapping//добавляет картинку при добавлении товара
    public String addImageProduct(@RequestParam("image") MultipartFile image, ProductDto productDto) throws IOException {
        Image img = imageService.uploadImage(image, productDto.getTitle());
        productService.save(productDto, img);
        return "redirect:/products/";
    }

    @GetMapping("/{id}")
    public String getOne(Model model, @PathVariable String id) {
        //находим наш продукт по id
        Optional<Product> productOptional = productService.getOneById(UUID.fromString(id));

        if (productOptional.isPresent()) {
            Product product = productOptional.get();//закидываем его в продукт
            List<Review> reviews = reviewService.getReviewsByProduct(product);//заполняем лист с отзывами
            model.addAttribute("product", product);//добавляем все в модель
            model.addAttribute("reviews", reviews);
        }
        return "product";
    }

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String id) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = imageService.loadFileAsResource(id);
        if (bufferedImage != null) {
            ImageIO.write(bufferedImage,"png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return new byte[0];
        }
    }

//    @PostMapping//добавляет картинку при добавлении товара
//    public String addImageReview(@RequestParam("image") MultipartFile image, ReviewDto reviewDto) throws IOException {
//        Image img = imageService.uploadImage(image, reviewDto.getId().toString());
//        reviewService.save(reviewDto,img);
//        return "redirect:/products/";
//    }

    @PostMapping("/reviews")
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