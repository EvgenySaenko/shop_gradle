package ru.geekbrains.shop.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.geekbrains.shop.persistence.entities.Image;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    @Query(value = "SELECT image.name FROM image INNER JOIN product p ON image.id = p.image WHERE p.id = :id", nativeQuery = true)
    String getImageNameByProduct(@Param("id") UUID id);


    @Query(value = "SELECT image.name FROM image INNER JOIN review r ON image.id = r.image WHERE r.id = :id", nativeQuery = true)
    String getImageNameByReview(@Param("id") UUID id);
}