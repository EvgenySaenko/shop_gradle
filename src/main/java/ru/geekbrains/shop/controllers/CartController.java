package ru.geekbrains.shop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import ru.geekbrains.shop.beans.Cart;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Api("Набор методов для корзины магазина")
public class CartController {

    private final Cart cart;
    private final ProductService productService;

    @GetMapping("/decrement/{id}")
    public void decrementProductToCartById(@PathVariable UUID id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<Product> productOptional = productService.getOneById(id);
        if (productOptional.isPresent()) {
            cart.decrement(productOptional.get());
            response.sendRedirect(request.getHeader("referer"));
        }
    }

    @GetMapping("/add/{id}")
    @ApiOperation(value = "Добавляет продукт в одном количестве в корзину")
    public void addProductToCart(@PathVariable UUID id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<Product> productOptional = productService.getOneById(id);
        if (productOptional.isPresent()) {
            cart.add(productOptional.get());
            response.sendRedirect(request.getHeader("referer"));
        }
    }

    @GetMapping("/remove/{id}")
    @ApiOperation(value = "Удаляет продукт в одном количестве из корзины", response = String.class)
    public String removeProductFromCart(@PathVariable UUID id) {
        cart.removeByProductId(id);
        return "redirect:/cart";
    }

    @GetMapping
    @ApiOperation(value = "Показывает что лежит в данный момент в корзине", response = String.class)
    public String showCart(Model model) {
        model.addAttribute("cart", cart);
        return "cart";
    }

//    //студента достали добавили в форму и показали
//    @GetMapping("/edit/{id}")
//    public String showEditFormCart(@PathVariable UUID id, Model model){
//        model.addAttribute("cartRecord", cart.getCartRecordById(id));
//        return "edit_form_cart";
//    }
//
//    @PostMapping("/edit")
//    public String modifyCartRecord(@ModelAttribute CartRecord cartRecord){
//        cart.edit(cartRecord);
//        return "redirect:/cart";
//    }
}