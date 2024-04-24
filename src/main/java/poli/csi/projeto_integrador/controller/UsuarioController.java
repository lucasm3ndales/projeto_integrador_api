package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filtro.UsuarioFiltro;
import poli.csi.projeto_integrador.dto.request.UsuarioAlterarDto;
import poli.csi.projeto_integrador.dto.request.UsuarioSalvarDto;
import poli.csi.projeto_integrador.model.Usuario;
import poli.csi.projeto_integrador.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
@AllArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/salvar")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody UsuarioSalvarDto dto) {
        boolean res = usuarioService.salvar(dto);
        if (res) {
            return ResponseEntity.ok("Usuário cadastrado com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao cadastrar usuário!");
    }

    @PutMapping("/alterar")
    public ResponseEntity<?> alterarUsuario(@Valid @RequestBody UsuarioAlterarDto dto) {
        boolean res = usuarioService.alterar(dto);
        if (res) {
            return ResponseEntity.ok("Dados do usuário alterados com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar dados do usuário!");
    }

    @PutMapping("/alterar/status/{id}")
    public ResponseEntity<?> alterarUsuario(@PathVariable("id") Long id) {
        boolean res = usuarioService.alterarStatus(id);
        if (res) {
            return ResponseEntity.ok("Status do usuário alterado com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar status do usuário!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable("id") Long id) {
        Usuario res = usuarioService.buscarUsuario(id);
        if (res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios")
    public ResponseEntity<Page<Usuario>> buscarUsuarios(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "ativo", required = false) Boolean ativo
    ) {
        UsuarioFiltro filtro = new UsuarioFiltro(search, role, ativo);
        Page<Usuario> res = usuarioService.buscarUsuarios(pageable, filtro);
        if (res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }
}
