package ru.geekbrains.shop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ShopController {

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/")
    public String homepage() {
        return "index";
    }

    @GetMapping("/profile")
    public String profilePage() {
        return "profile";
    }

}
