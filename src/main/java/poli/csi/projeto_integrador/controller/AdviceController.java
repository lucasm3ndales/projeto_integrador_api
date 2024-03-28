package poli.csi.projeto_integrador.controller;


import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import poli.csi.projeto_integrador.exception.CustomException;

import java.io.IOException;

@RestControllerAdvice
public class AdviceController {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authenticationExceptionHandler() {
        return ResponseEntity.badRequest().body("Credenciais inválidas!");
    }

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<String> jwtCreationExceptionHandler() {
        return ResponseEntity.internalServerError().body("Erro ao gerar token!");
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<String> jwtVerificationExceptionHandler() {
        return ResponseEntity.badRequest().body("Token inválido ou expirado!");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernameNotFoundExceptionHandler(UsernameNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> entityExistsExceptionHandler(EntityExistsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> customExceptionHandler(CustomException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> exceptionHandler(Exception ex) {
        return ResponseEntity.internalServerError().body("Erro interno do servidor!");
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<String> nullPointerExceptionHandler() {
        return ResponseEntity.internalServerError().body("Erro interno do servidor!");
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<String> ioExceptionHandler() {
        return ResponseEntity.internalServerError().body("Erro interno do servidor!");
    }
}
