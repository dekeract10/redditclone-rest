package rs.ac.uns.ftn.redditclonesr272020.services;

import lombok.extern.slf4j.XSlf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${image.path}")
    private String imagePath;

    Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public String saveImage(MultipartFile image) throws IOException {
        logger.info("image size: {}", image.getSize());
        UUID imageName = UUID.randomUUID();
        Path path = Paths.get(imagePath, imageName.toString());
        logger.info("Image path: {}", path);
        try {
            Files.write(path, image.getBytes());
        } catch (Exception e) {
            logger.error("Error saving image: {}", e.getMessage());
            throw new IOException(e);
        }
        return imageName.toString();
    }
}
