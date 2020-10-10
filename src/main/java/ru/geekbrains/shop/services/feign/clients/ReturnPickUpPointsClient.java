package ru.geekbrains.shop.services.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ReturnPickUpPointsClient",url = "${documents.service.url}")
public interface ReturnPickUpPointsClient {

    @GetMapping(value = "/points")
    ResponseEntity<byte[]> getJsonFile();

}
