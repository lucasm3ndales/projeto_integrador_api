package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filter.FiltroServidor;
import poli.csi.projeto_integrador.dto.request.AlterarServidorDto;
import poli.csi.projeto_integrador.dto.request.SalvarServidorDto;
import poli.csi.projeto_integrador.dto.response.StatusResDto;
import poli.csi.projeto_integrador.model.Servidor;
import poli.csi.projeto_integrador.service.ServidorService;

@RestController
@RequestMapping("/servidor")
@AllArgsConstructor
public class ServidorController {
    private final ServidorService servidorService;

    @PostMapping("/salvar")
    public ResponseEntity<String> salvarServidor(@Valid @RequestBody SalvarServidorDto dto) {
        boolean res = servidorService.salvarServidor(dto);
        if(res) {
            return ResponseEntity.ok("Cadastro efetuado com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao fazer cadastro!");
    }

    @PutMapping("/alterar")
    public ResponseEntity<String> alterarServidor(@Valid @RequestBody AlterarServidorDto dto) {
        boolean res = servidorService.alterarServidor(dto);
        if(res) {
            return ResponseEntity.ok("Dados do servidor alterados com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro alterar dados do servidor!");
    }

    @PutMapping("/alterar/status/{id}")
    public ResponseEntity<?> alterarStatusServidor(@PathVariable("id") Long id) {
        StatusResDto res = servidorService.alterarStatusServidor(id);
        if(res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.internalServerError().body("Erro alterar status do servidor!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarServidor(@PathVariable("id") Long id) {
        if(id != null) {
            Servidor res = servidorService.buscarServidor(id);
            if(res != null) {
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().body("Id do servidor nulo!");
    }

    @GetMapping("/servidores")
    public ResponseEntity<Page<Servidor>> buscarServidores(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "matricula", required = false) String matricula
    ) {
        FiltroServidor filtro = new FiltroServidor(nome, matricula);
        Page<Servidor> res = servidorService.buscarServidores(pageable, filtro);
        if(res.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res);
    }
}
