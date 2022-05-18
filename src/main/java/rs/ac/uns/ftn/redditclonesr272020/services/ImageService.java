package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ImageService {
    String saveImage(MultipartFile image) throws IOException;
}
