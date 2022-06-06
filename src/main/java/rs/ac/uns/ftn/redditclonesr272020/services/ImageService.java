package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

@Service
public interface ImageService {
    String saveImage(MultipartFile image) throws IOException;

    Optional<Resource> getImage(String imgPath) throws MalformedURLException;

    void delete(String imagePath);
}
