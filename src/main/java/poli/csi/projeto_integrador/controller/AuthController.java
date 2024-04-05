package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.request.AlterarLoginDto;
import poli.csi.projeto_integrador.dto.request.AuthReqDto;
import poli.csi.projeto_integrador.dto.response.AuthResDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.service.AuthService;
import poli.csi.projeto_integrador.service.UsuarioService;

@RestController()
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UsuarioService usuarioService;

    //Pode retornar ou um "Usuário" ou uma mensagem adequada com uma custom exception
    @PostMapping
    public ResponseEntity<?> auth(@RequestBody AuthReqDto req) {
        AuthResDto res = authService.authUsuario(req);
        if(res != null) {
            return ResponseEntity.ok().body(res);
        }
        return ResponseEntity.badRequest().body("Erro ao autenticar usuário!");
    }

    //TODO: Testar depois
    @PutMapping("/alterar")
    public ResponseEntity<String> alterarLogin(@Valid @RequestBody AlterarLoginDto dto) {
        boolean res = usuarioService.alterarLogin(dto);
        if(res) {
            return ResponseEntity.ok("Dados do usuário alterados com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar dados do usuário!");
    }
}
