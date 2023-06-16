package ekan.ekanavaliacaobackend.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.FieldError;

@Data
@AllArgsConstructor
public class ValidationExceptionDataDTO {

    private String field;
    private String message;

    public ValidationExceptionDataDTO(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }

}
