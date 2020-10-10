package ru.geekbrains.shop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.geekbrains.paymentservice.Payment;
import ru.geekbrains.shop.beans.Cart;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.services.ProductService;
import ru.geekbrains.shop.services.soap.PaymentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Api("Набор методов для корзины магазина")
public class CartController {

    private final Cart cart;
    private final ProductService productService;

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

}