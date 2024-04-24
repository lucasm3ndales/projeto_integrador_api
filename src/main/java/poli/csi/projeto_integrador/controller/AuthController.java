package poli.csi.projeto_integrador.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.request.AuthReqDto;
import poli.csi.projeto_integrador.dto.response.AuthResDto;
import poli.csi.projeto_integrador.service.AuthService;

@RestController()
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> auth(@RequestBody AuthReqDto req) {
        AuthResDto res = authService.authUsuario(req);
        if(res != null) {
            return ResponseEntity.ok().body(res);
        }
        return ResponseEntity.badRequest().body("Erro ao autenticar usuário!");
    }

}
