package ru.geekbrains.shop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api("Набор методов для витрины онлайн-магазина")
public class ProductController {

    private final ImageService imageService;
    private final ProductService productService;
    private final ReviewService reviewService;



    @GetMapping("/")
    @ApiOperation(value = "Выводит на сраницу все продукты данной категории(если указана)", response = String.class)
    public String allProducts(Model model, @RequestParam(required = false) Integer category) {
        model.addAttribute("products", productService.getAll(category));
        return "products";
    }



    @PostMapping//добавляет картинку и новый товар(админ)
    @ApiOperation(value = "Добавляет картинку при добавлении товара", response = String.class)
    public String addImageProduct(@RequestParam("image") MultipartFile image, ProductDto productDto) throws IOException {
        Image img = imageService.uploadImage(image, productDto.getTitle());
        productService.save(productDto, img);
        return "redirect:/products/";
    }

    @ApiOperation(value = "Получить продукт в одном количестве", response = String.class)
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
    @ApiOperation(value = "Возвращает изображение(массив байтов)", response = Byte.class)
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






    //    @RabbitListener(queues = "super-shop.queue")
//    public void listenTo(Message message) {
//        try {
//            String reqMessage = new String(message.getBody(), StandardCharsets.UTF_8);
//            System.out.println(reqMessage);
//        } catch (Throwable th) {
//            log.error("Fatal error: can't process Generated Report response", th);
//        }
//    }

}