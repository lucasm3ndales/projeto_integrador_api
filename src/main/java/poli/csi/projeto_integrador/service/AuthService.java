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
import poli.csi.projeto_integrador.model.User;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    public AuthResDto authUsuario(AuthReqDto req) throws AuthenticationException {
        try {
            Authentication credentials = new UsernamePasswordAuthenticationToken(req.username().trim(), req.password().trim());
            Authentication auth = authenticationManager.authenticate(credentials);
            UserDetails userDetails = userService.loadUserByUsername(auth.getName());
            User user = userService.getUserByUsername(auth.getName());
            String token = tokenService.createToken(userDetails);
            AuthResDto res = new AuthResDto(
                    user.getId(),
                    user.getName(),
                    user.getActive(),
                    user.getRole().name(),
                    token
            );
            return res;
        } catch (AuthenticationException ex) {
            throw ex;
        }
    }
}
