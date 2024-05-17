package poli.csi.projeto_integrador.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poli.csi.projeto_integrador.model.Document;
import poli.csi.projeto_integrador.service.DocumentService;

@RestController
@RequestMapping("/document")
@AllArgsConstructor
public class DocumentController {
    private final DocumentService documentService;


    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable("id") Long idDocument) {
        Document document = documentService.getDocument(idDocument);

        if (document != null) {
            byte[] content = document.getDoc();
            String extension = document.getExtension().name().toLowerCase();
            String name = document.getName();
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.parseMediaType(getContentTypeByFileExtension(extension)));
            headers.setContentDispositionFormData("attachment", name + "." + extension);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    private String getContentTypeByFileExtension(String extension) {
        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "doc":
                return "application/msword";
            case "txt":
                return "text/plain";
            case "odt":
                return "application/vnd.oasis.opendocument.text";
            default:
                return "application/octet-stream";
        }
    }


}
