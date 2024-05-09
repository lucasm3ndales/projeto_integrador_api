package poli.csi.projeto_integrador.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import poli.csi.projeto_integrador.service.TokenService;
import poli.csi.projeto_integrador.service.UserService;
import java.io.IOException;

@Component
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc) throws ServletException, IOException {
        String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader != null) {
            String token = authHeader.replace("Bearer",  "").trim();
            if(token != null) {
                String subject = tokenService.getSubject(token);
                UserDetails userDetails = userService.loadUserByUsername(subject);
                UsernamePasswordAuthenticationToken authenticationJwt = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationJwt);
            }
        }
        fc.doFilter(req, res);
    }
}
