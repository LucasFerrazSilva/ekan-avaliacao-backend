package ekan.ekanavaliacaobackend.domain.documento;

import ekan.ekanavaliacaobackend.domain.beneficiario.Beneficiario;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DocumentoDTO {

    private Long id;
    private String tipoDocumento;
    private String descricao;
    private LocalDateTime dataInclusao;
    private LocalDateTime dataAtualizacao;
    private Beneficiario beneficiario;

}
