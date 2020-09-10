package ru.geekbrains.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.shop.persistence.repositories.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService{

    private final ImageRepository imageRepository;

    private String getImageForSpecificProduct(UUID id) {
        return imageRepository.obtainImageNameByProduct(id);
    }

    public BufferedImage loadFileAsResource(String id) throws IOException {
        try {
            String imageName = getImageForSpecificProduct(UUID.fromString(id));
            UrlResource urlResource = new UrlResource("file:F:/Spring/uploads/images/"+imageName);
            UrlResource urlResourceError = new UrlResource("file:F:/Spring/uploads/images/errorImage.png");

//            Resource resource = new ClassPathResource("/static/images/" + imageName);
//            Resource resourceError = new ClassPathResource("/static/images/errorImage.png");
            if (urlResource.exists()) {
                return ImageIO.read(urlResource.getFile());
            }else {
                log.error("Image not found!");
                return  ImageIO.read(urlResourceError.getFile());
               // throw new FileNotFoundException("File " + imageName + " not found!");
            }
        } catch (MalformedInputException | FileNotFoundException ex) {
            return null;
        }
    }

    public void saveImage(MultipartFile imageFile)throws IOException{
        UrlResource urlResource = new UrlResource("file:F:/Spring/uploads/images/");
        byte [] bytes = imageFile.getBytes();
        Path pathImage = Paths.get(urlResource.toString() + imageFile.getOriginalFilename());
        Files.write(pathImage,bytes);
    }



}