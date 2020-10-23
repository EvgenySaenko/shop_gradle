//package ru.geekbrains.shop.controllers;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import org.junit.Before;
//import org.junit.runner.RunWith;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import ru.geekbrains.shop.beans.Cart;
//import ru.geekbrains.shop.persistence.entities.OrderItem;
//import ru.geekbrains.shop.persistence.entities.Product;
//import ru.geekbrains.shop.services.ProductService;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(controllers = CartController.class)
//public class CartControllerMVCTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private Cart cart;
//
//    private OrderItem itemsMock;
//
//    @MockBean
//    private ProductService productServiceMock;
//
//    @MockBean
//    private Product productMock;
//
//    {
//        try{
//            productMock = new ObjectMapper().readValue(new ClassPathResource("mocks/product.json").getFile(), new TypeReference<>(){});
//        } catch (JsonParseException e) {
//            e.printStackTrace();
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Before
//    public void setUp() {
//        cart.add(productMock);//добавили в корзину одну покупку - сникерс 1 шт
//
//        itemsMock = itemsMock.builder()//подготовили обновленную покупку
//                .price(BigDecimal.valueOf(70.00))
//                .product(productMock)
//                .quantity(5)
//                .order(null)
//        .build();
//
//
//    }
//
//    public void testMustEditQuantity() throws Exception {
//        mockMvc
//                .perform(post("/edit")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(redirectedUrl("/cart"))
//                .andExpect(status().isOk()
//        );
//    }
//
//}
