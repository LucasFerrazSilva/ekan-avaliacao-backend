package ekan.ekanavaliacaobackend.domain.beneficiario;

import ekan.ekanavaliacaobackend.domain.documento.Documento;
import ekan.ekanavaliacaobackend.domain.documento.DocumentoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor @NoArgsConstructor
public class BeneficiarioComDocumentosDTO {

    private Long id;
    private String nome;
    private String telefone;
    private LocalDate dataNascimento;
    private LocalDateTime dataInclusao;
    private LocalDateTime dataAtualizacao;
    private List<DocumentoDTO> documentos;

    public BeneficiarioComDocumentosDTO(Beneficiario beneficiario) {
        this(
                beneficiario.getId(),
                beneficiario.getNome(),
                beneficiario.getTelefone(),
                beneficiario.getDataNascimento(),
                beneficiario.getDataInclusao(),
                beneficiario.getDataAtualizacao(),
                beneficiario.getDocumentos().stream().map(Documento::toDTO).collect(toList())
        );
    }

}
