package ekan.ekanavaliacaobackend.domain.beneficiario;

import ekan.ekanavaliacaobackend.domain.documento.NovoDocumentoDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data @AllArgsConstructor
public class NovoBeneficiarioDTO {

    @NotBlank
    private String nome;
    @NotBlank
    private String telefone;
    @NotBlank
    private LocalDate dataNascimento;
    @NotEmpty
    private List<NovoDocumentoDTO> documentosDTOs;

}
