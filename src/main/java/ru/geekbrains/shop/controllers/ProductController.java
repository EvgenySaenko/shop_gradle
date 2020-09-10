package ru.geekbrains.shop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.services.ImageService;
import ru.geekbrains.shop.services.ProductService;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ImageService imageService;
    private final ProductService productService;

    @GetMapping("/")
    public String allProducts(Model model, @RequestParam(required = false) Integer category) {
        model.addAttribute("products", productService.getAll(category));
        return "products";
    }

    @GetMapping(value = "/{id}")
    public String getOne(Model model, @PathVariable String id) {
//        Optional<Product>productOptional = productService.getOneById(UUID.fromString(id));
//        productOptional.ifPresent(product -> model.addAttribute("product",product));
        //можно кратко
        productService.getOneById(UUID.fromString(id))
                .ifPresent(product -> model.addAttribute("product",product));
        return "product";
    }

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String id) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(imageService.loadFileAsResource(id), "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }




}