package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import poli.csi.projeto_integrador.dto.request.AuthReqDto;
import poli.csi.projeto_integrador.dto.response.AuthResDto;
import poli.csi.projeto_integrador.service.AuthService;

@RestController(value = "/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    //Pode retornar ou um "Usuário" ou uma mensagem adequada
    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody AuthReqDto req) {
        AuthResDto res = authService.authUsuario(req);
        if(res != null) {
            return ResponseEntity.ok().body(res);
        }
        return ResponseEntity.internalServerError().body("Erro ao autenticar usuário.");
    }
}
