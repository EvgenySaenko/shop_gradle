package ru.geekbrains.shop.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.geekbrains.shop.controllers.ShopController;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.persistence.entities.Review;
import ru.geekbrains.shop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.shop.persistence.repositories.ReviewRepository;
import ru.geekbrains.shop.services.feign.clients.ShopFeignClient;

import java.io.IOException;


import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReviewServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    @MockBean//инжектим имитацию бина, чтобы легковесно прогнать тест нашего MVC слоя не поднимая ApplicationContext
    private ShopFeignClient shopFeignClientMock;

    @MockBean//этот тоже, падал тест, значит он взаимодействует тоже где то(там заинжекчен фейгн)
    private ShopController shopControllerMock;

    @MockBean
    private AmqpTemplate amqpTemplateMock;

    @MockBean
    private Review reviewMock;

    @MockBean
    private Product productMock;


    //при запуске теста, запустится этот статический инициализатор =>
    // прочитает JSON => замапит на суность Review и сделает лист комментариев reviewsMocks
    {
        try{
            reviewMock = new ObjectMapper().readValue(new ClassPathResource("mocks/review.json").getFile(), new TypeReference<>(){});
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    //когда мы обратимся к нашему reviewServiceMock и вызовим у него getAll() => он нам вернет reviewsMocks
    //то есть все те комментарии которые мы вычитали с JSON-а, то есть он как бы возьмет их с этого подготовленного выше списка коментов
    public void setUp() {
        productMock = Product.builder()
                .title("Лимонад")
                .category(ProductCategory.DRINK)
                .price(80.00)
                .description(null)
                .available(true)
        .build();
        reviewService.save(reviewMock);
    }

    @Test
    public void testAmountReviews(){
        assertEquals(1,reviewService.getReviewsByProduct(productMock).size());
    }
}
