package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.redditclonesr272020.services.PdfService;

import java.util.Optional;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {
    @Autowired
    private PdfService pdfService;
    @GetMapping("{pdf}")
    public ResponseEntity<Resource> getPdf(@PathVariable("pdf") String pdfPath) {
        try {
            var fileOpt = pdfService.getPdf(pdfPath);
            if (fileOpt.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            long length = fileOpt.get().contentLength();

            var filename = Optional.ofNullable(fileOpt.get().getFilename());
            var cdb = ContentDisposition.attachment().filename(filename.orElse("dummy.pdf")).build();

            HttpHeaders hdr = new HttpHeaders();
            hdr.setContentDisposition(cdb);
            return ResponseEntity.ok().headers(hdr).contentLength(length).contentType(MediaType.APPLICATION_PDF).body(fileOpt.get());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
