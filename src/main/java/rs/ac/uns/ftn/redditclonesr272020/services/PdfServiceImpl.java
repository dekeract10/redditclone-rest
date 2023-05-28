package rs.ac.uns.ftn.redditclonesr272020.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class PdfServiceImpl implements PdfService {
    @Value(value = "${pdf.path}")
    private String pdfDir;
    Logger logger = LoggerFactory.getLogger(PdfServiceImpl.class);

    @Override
    public Optional<String> saveFile(MultipartFile pdf) {
        try {
            logger.info("PDF size: {}", pdf.getSize());
            var pdfName = UUID.randomUUID() + ".pdf";
            Path path = Paths.get(pdfDir, pdfName);
            logger.info("PDF path: {}", path);
            pdf.transferTo(path);
            return Optional.of(pdfName);
        } catch (Exception e) {
            logger.error("Error saving PDF: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> parsePdf(MultipartFile file) {
        try (var pdfInputStream = file.getInputStream(); var pddDocument = PDDocument.load(pdfInputStream);) {
            var pdfStripper = new PDFTextStripper();
            return Optional.of(pdfStripper.getText(pddDocument));
        } catch (Exception e) {
            logger.error("Error parsing PDF {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Resource> getPdf(String pdfPath) throws MalformedURLException {
        Path path = Paths.get(pdfDir).resolve(pdfPath);
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return Optional.of(resource);
            }
        } catch (Exception e) {
            logger.error("Couldn't load pdf: {}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(String pdfPath) {
        Path path = Paths.get(pdfDir).resolve(pdfPath);
        try {
            Files.deleteIfExists(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

