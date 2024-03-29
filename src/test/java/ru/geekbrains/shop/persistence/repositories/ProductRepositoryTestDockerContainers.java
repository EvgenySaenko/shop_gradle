package ru.geekbrains.shop.persistence.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import org.testcontainers.containers.PostgreSQLContainer;
import ru.geekbrains.shop.persistence.entities.Product;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

//при тестировании через докер контенер, с помошью нашего фреймворка создасться
//виртуальный докер контейнер
//внутри него создастся виртуальная база Постгрес
//на ней прогонятся все наши тесты - после тестирования контейнер пропадает


@DataJpaTest//Replace.NONE означает что автоконфигурация нам не нужна - за все отвечать будут докер контейнеры
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {ProductRepositoryTestDockerContainers.Initializer.class})
public class ProductRepositoryTestDockerContainers {

    //означает выкачай нам изображение(образ) с названием - postgres
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext){
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    private List<Product> productMocks;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp(){
        try {
            productMocks = new ObjectMapper().readValue(new ClassPathResource("mocks/products.json").getFile(), new TypeReference<>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        productMocks.forEach(product -> productRepository.save(product));
    }

    @Test
    public void mustFindAllProducts(){
        Assert.assertEquals(4,productRepository.findAll().size());
    }
}
