package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.redditclonesr272020.services.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    Logger logger = LoggerFactory.getLogger(ImageController.class);

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile image) {
        logger.info("Uploading image {}", image);
        try {
            var imgPath = imageService.saveImage(image);
            return ResponseEntity.ok(imgPath);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
