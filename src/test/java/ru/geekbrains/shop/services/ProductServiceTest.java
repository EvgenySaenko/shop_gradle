package ru.geekbrains.shop.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.geekbrains.shop.controllers.ShopController;
import ru.geekbrains.shop.dto.ProductDto;
import ru.geekbrains.shop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.shop.services.feign.clients.ShopFeignClient;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean//инжектим имитацию бина, чтобы легковесно прогнать тест нашего MVC слоя не поднимая ApplicationContext
    private ShopFeignClient shopFeignClientMock;

    @MockBean//этот тоже, падал тест, значит он взаимодействует тоже где то(там заинжекчен фейгн)
    private ShopController shopControllerMock;

    @Before
    public void setUp() {
        ProductDto productDto = ProductDto.builder()
                .title("Кофе")
                .available(true)
                .category(ProductCategory.DRINK)
                .price(200.00)
                .description("")
        .build();

        productService.save(productDto, null);
    }

    @Test
    public void testSomething(){
        assertEquals(1,productService.getAll(0).size());
    }
}
