package poli.csi.projeto_integrador.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.AuthReqDto;
import poli.csi.projeto_integrador.dto.response.AuthResDto;
import poli.csi.projeto_integrador.model.Usuario;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;
    public AuthResDto authUsuario(AuthReqDto req) throws AuthenticationException {
        try {
            Authentication credenciais = new UsernamePasswordAuthenticationToken(req.login().trim(), req.senha().trim());
            Authentication auth = authenticationManager.authenticate(credenciais);
            UserDetails userDetails = usuarioService.loadUserByUsername(auth.getName());
            Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
            String token = tokenService.createToken(userDetails);
            AuthResDto res = new AuthResDto(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getAtivo(),
                    usuario.getRole().name(),
                    token
            );
            return res;
        } catch (AuthenticationException ex) {
            throw ex;
        }
    }
}
