package poli.csi.projeto_integrador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

import java.util.List;

public class ValidationError implements ErrorResponse {
    private String mensagem;

    public ValidationError(List<String> errors) {
        this.mensagem = String.join(", ", errors);
    }
    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ProblemDetail getBody() {
       ProblemDetail pb =  ProblemDetail.forStatus(this.getStatusCode());
       pb.setTitle("Erro de validação!");
       pb.setDetail(this.mensagem);
       return pb;
    }
}
