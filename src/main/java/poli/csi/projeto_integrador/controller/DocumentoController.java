package poli.csi.projeto_integrador.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.model.DocumentoEvento;
import poli.csi.projeto_integrador.service.DocumentoService;

import java.util.Random;

@RestController
@RequestMapping("/documento")
@AllArgsConstructor
public class DocumentoController {
    private DocumentoService documentoService;


    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable("id") Long id) {
        DocumentoEvento res = documentoService.download(id);
        if(res == null) {
            return ResponseEntity.notFound().build();
        }
        String extensao = res.getExtensao().name().toLowerCase();
        Random random = new Random();
        String numAleatorio = Integer.toString(random.nextInt(10000));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", res.getNome() + "_" + numAleatorio + "." + extensao);
        return new ResponseEntity<>(res.getDoc(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{docId}/{usuarioId}")
    public ResponseEntity<String> download(@PathVariable("docId") Long docId, @PathVariable("usuarioId") Long usuarioId) {
        boolean res = documentoService.deletarDocumento(docId, usuarioId);
        if(res) {
            return ResponseEntity.ok("Documento deletado com sucesso!");
        }
       return ResponseEntity.internalServerError().body("Erro ao deletar documento!");
    }
}
