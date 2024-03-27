package poli.csi.projeto_integrador.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${token.key}")
    private String tokenKey;

    public String createToken(UserDetails usuario) throws JWTCreationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenKey);
            return JWT.create().withIssuer("gestfair")
                    .withSubject(usuario.getUsername())
                    .withClaim("role", usuario.getAuthorities().stream().toList().get(0).toString()).withExpiresAt(expirationDate()).sign(algorithm);
        } catch (JWTCreationException ex) {
            throw ex;
        }
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String jwt) throws JWTVerificationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenKey);
            return JWT.require(algorithm).withIssuer("gestfair").build().verify(jwt).getSubject();
        } catch (JWTVerificationException ex) {
            throw ex;
        }
    }

}
