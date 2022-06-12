package rs.ac.uns.ftn.redditclonesr272020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    private static final int TARGET_MP_SIZE = 1_000_000;

    private BufferedImage scaledImage(InputStream stream) {
        try {
            var bufferedImage = ImageIO.read(stream);
            int w = bufferedImage.getWidth();
            int h = bufferedImage.getHeight();
            if ((w * h) < TARGET_MP_SIZE) {
                stream.close();
                return bufferedImage;
            } else {
                logger.info("Original size {}✕{}", w, h);
                float ratio = (float) h / w;
                var targetWidth = Math.round((float) Math.sqrt(TARGET_MP_SIZE / ratio));
                var targetHeight = Math.round(targetWidth * ratio);

                logger.info("Calculated size {}✕{}", targetWidth, targetHeight);

                Image resultingImage = bufferedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST);
                BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
                outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
                stream.close();
                return outputImage;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String saveImage(MultipartFile image) throws IOException {
        logger.info("image size: {}", image.getSize());
        UUID imageName = UUID.randomUUID();
        Path path = Paths.get(imageDir, imageName.toString());
        var scaledImage = scaledImage(image.getInputStream());
        logger.info("Image path: {}", path);
        try {
            if (scaledImage != null) {
//                ImageIO.write(scaledImage, ".jpg", path.toFile());
                ImageIO.write(scaledImage, "jpg", path.toFile());
//                Files.write(path, scaledImage.);
            }
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
