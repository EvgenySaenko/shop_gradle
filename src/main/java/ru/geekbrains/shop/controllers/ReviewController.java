package ru.geekbrains.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.geekbrains.shop.services.ReviewService;
import ru.geekbrains.shop.services.feign.clients.ShopFeignClient;

import java.util.UUID;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {




    private final ReviewService reviewService;


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//доступ только для админа
    public String moderateReview(@PathVariable UUID id, @RequestParam String option){
        return "redirect:/products/" + reviewService.moderate(id,option);
    }

}
