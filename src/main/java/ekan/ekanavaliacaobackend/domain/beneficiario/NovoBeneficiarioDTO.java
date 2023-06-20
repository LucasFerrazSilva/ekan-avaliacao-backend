package ekan.ekanavaliacaobackend.domain.beneficiario;

import ekan.ekanavaliacaobackend.domain.documento.Documento;
import ekan.ekanavaliacaobackend.domain.documento.NovoDocumentoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data @AllArgsConstructor
public class NovoBeneficiarioDTO {

    @NotBlank
    private String nome;
    @NotBlank
    private String telefone;
    @NotNull
    private LocalDate dataNascimento;
    @Valid
    @NotEmpty
    private List<NovoDocumentoDTO> documentosDTOs;

}
