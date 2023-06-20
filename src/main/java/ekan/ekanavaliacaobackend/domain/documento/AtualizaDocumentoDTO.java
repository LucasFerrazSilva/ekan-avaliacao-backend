package ekan.ekanavaliacaobackend.domain.documento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AtualizaDocumentoDTO {

    @NotNull
    private Long id;
    @NotBlank
    private String tipoDocumento;
    @NotBlank
    private String descricao;
}
