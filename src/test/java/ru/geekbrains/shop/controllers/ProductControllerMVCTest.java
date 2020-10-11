package ru.geekbrains.shop.controllers;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.persistence.entities.Review;
import ru.geekbrains.shop.services.ImageService;
import ru.geekbrains.shop.services.ProductService;
import ru.geekbrains.shop.services.ReviewService;
import ru.geekbrains.shop.services.ShopuserService;

import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ReviewController.class)
public class ProductControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageServiceMock;

    @MockBean
    private ProductService productServiceMock;

    @MockBean
    private ReviewService reviewServiceMock;

    @MockBean
    private ShopuserService shopuserServiceMock;

    private List<Product> reviewsMocks;
}
