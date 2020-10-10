
package ru.geekbrains.shop.services.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//обозначаем что это клиент, указываем юрл-который указали в application.yaml файле
@FeignClient(name = "ShopFeignClient", url = "${documents.service.url}")
public interface ShopFeignClient {

    @GetMapping(value = "/flyers")//указываем конечный эндпоинт
    ResponseEntity<byte[]> getFlyer();

}
