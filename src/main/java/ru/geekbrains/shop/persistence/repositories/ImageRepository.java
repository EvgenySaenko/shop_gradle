package ru.geekbrains.shop.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.shop.persistence.entities.Image;

import java.io.IOException;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    @Query(value = "SELECT image.name FROM image INNER JOIN product p ON image.id = p.image WHERE p.id = :id", nativeQuery = true)
    String obtainImageNameByProduct(@Param("id") UUID id);

    //void saveImage(MultipartFile imageFile) throws IOException;
}