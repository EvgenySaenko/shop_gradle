package ru.geekbrains.shop.persistence.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.geekbrains.shop.persistence.entities.Product;
import ru.geekbrains.shop.persistence.entities.enums.ProductCategory;

import java.io.IOException;
import java.util.List;

@DataJpaTest
@RunWith(SpringRunner.class)//значает что приложение будет запущено в процессе тестирования
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired//инжектим реальный наш продуктовый репозиторий
    private ProductRepository productRepository;

    private List<Product> productMocks;
    //при запуске теста, запустится этот статический инициализатор =>
    // прочитает JSON => замапит на суность Product и сделает лист продуктов productMocks
    {
        try {
            productMocks = new ObjectMapper().readValue(new ClassPathResource("mocks/products.json").getFile(), new TypeReference<>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@BeforeClass чтобы метод setUp выполнился один раз, но он должен быть static
    @Before//при вызове каждого теста будет вызываться метод setUp => выполнит всю необходимую бизнес логику(которая внутри этого метода)
    public void setUp(){
        productMocks.forEach(//наполняем наш персист слой тестовыми данными(создадим виртуальную базу данных и сможем с ней работать)
                product -> testEntityManager.persist(product)
        );
    }
    @Test
    public void mustFindAllDrinks(){
        Assert.assertEquals(1,productRepository.findAllByCategory(ProductCategory.DRINK).size());
    }

    @Test
    public void mustFindAllFoods(){
        Assert.assertEquals(3,productRepository.findAllByCategory(ProductCategory.FOOD).size());
    }

    @Test
    public void mustFindNoProducts(){
        Assert.assertEquals(0,productRepository.findAllByCategory(null).size());
    }

    @Test
    public void mustAllProducts(){
        Assert.assertEquals(4,productRepository.findAll().size());
    }
}
