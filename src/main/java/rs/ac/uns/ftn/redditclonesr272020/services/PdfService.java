package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.Optional;

@Service
public interface PdfService {
    /*
    Persists a file to disk and returns filename
     */
    Optional<String> saveFile(MultipartFile pdf);

    Optional<String> parsePdf(MultipartFile file);

    Optional<Resource> getPdf(String imgPath) throws MalformedURLException;

    boolean delete(String pdfPath);
}
