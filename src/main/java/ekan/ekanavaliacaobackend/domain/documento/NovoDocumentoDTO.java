package ekan.ekanavaliacaobackend.domain.documento;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class NovoDocumentoDTO {

    @NotBlank
    private String tipoDocumento;
    @NotBlank
    private String descricao;

}
