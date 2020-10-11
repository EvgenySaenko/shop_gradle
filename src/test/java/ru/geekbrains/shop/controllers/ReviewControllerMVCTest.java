package ru.geekbrains.shop.controllers;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ru.geekbrains.shop.persistence.entities.Review;
import ru.geekbrains.shop.services.ReviewService;
import ru.geekbrains.shop.services.ShopuserService;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean//инжектим имитацию бина, чтобы легковесно прогнать тест нашего MVC слоя не поднимая ApplicationContext
    private ReviewService reviewServiceMock;

    @MockBean
    private ShopuserService shopuserServiceMock;

//    @MockBean//если в настоящем контроллере например заинжекчен еще что то =>
//    // указываем их тут все и помечаем как MockBean чтобы не упал тест - т.к. он их не обнаружит
//    private ShopFeignClient shopFeignClientMock;
    private List<Review> reviewsMocks;

    //при запуске теста, запустится этот статический инициализатор =>
    // прочитает JSON => замапит на суность Review и сделает лист комментариев reviewsMocks
    {
        try{
            reviewsMocks = new ObjectMapper().readValue(new ClassPathResource("mocks/reviews.json").getFile(), new TypeReference<>(){});
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
        given(reviewServiceMock.getAll()).willReturn(reviewsMocks);
    }

    @Test
    public void testMustReturnAllAvailableReviews() throws Exception {
        mockMvc
                .perform(get("/reviews/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

}
