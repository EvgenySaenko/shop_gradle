package ru.geekbrains.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.geekbrains.shop.services.ImageService;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUploadController {

    private final ImageService imageService;

    @GetMapping("/")
    public String uploadPage() {
        return "admin";
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public String uploadImage(Model model, @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        if (imageFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }
        try {
            imageService.saveImage(imageFile);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + imageFile.getOriginalFilename() + "'");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error saving image", e);
        }
        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}