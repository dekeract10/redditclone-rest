package rs.ac.uns.ftn.redditclonesr272020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${image.path}")
    private String imageDir;

    Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public String saveImage(MultipartFile image) throws IOException {
        logger.info("image size: {}", image.getSize());
        UUID imageName = UUID.randomUUID();
        Path path = Paths.get(imageDir, imageName.toString());
        logger.info("Image path: {}", path);
        try {
            Files.write(path, image.getBytes());
        } catch (Exception e) {
            logger.error("Error saving image: {}", e.getMessage());
            throw new IOException(e);
        }
        return imageName.toString();
    }

    @Override
    public Optional<Resource> getImage(String imgPath) throws MalformedURLException {
        Path file = Paths.get(imageDir).resolve(imgPath);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return Optional.of(resource);
        }
        return Optional.empty();
    }

    @Override
    public void delete(String imagePath) {
        Path path = Paths.get(imageDir).resolve(imagePath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            logger.warn("Could not delete image {}", path);
        }
    }
}
