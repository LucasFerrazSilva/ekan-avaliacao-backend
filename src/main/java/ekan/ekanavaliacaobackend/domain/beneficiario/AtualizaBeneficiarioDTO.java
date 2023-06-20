package ekan.ekanavaliacaobackend.domain.beneficiario;

import ekan.ekanavaliacaobackend.domain.documento.AtualizaDocumentoDTO;
import ekan.ekanavaliacaobackend.domain.documento.NovoDocumentoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class AtualizaBeneficiarioDTO {

    @NotNull
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String telefone;
    @NotNull
    private LocalDate dataNascimento;
    @Valid
    @NotEmpty
    private List<AtualizaDocumentoDTO> documentosDTOs;
}
