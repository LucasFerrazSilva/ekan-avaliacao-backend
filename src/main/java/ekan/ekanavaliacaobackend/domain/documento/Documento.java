package ekan.ekanavaliacaobackend.domain.documento;

import ekan.ekanavaliacaobackend.domain.beneficiario.Beneficiario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

import static org.springframework.util.StringUtils.hasText;

@Entity
@Table(name="TB_DOCUMENTOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of="id")
@ToString(exclude="beneficiario")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipoDocumento;
    private String descricao;
    private LocalDateTime dataInclusao;
    private LocalDateTime dataAtualizacao;
    @ManyToOne
    @JoinColumn(name="beneficiario_id")
    private Beneficiario beneficiario;

    public Documento(NovoDocumentoDTO documentoDTO, Beneficiario beneficiario) {
        this.tipoDocumento = documentoDTO.getTipoDocumento();
        this.descricao = documentoDTO.getDescricao();
        this.dataInclusao = LocalDateTime.now();
        this.beneficiario = beneficiario;
    }

    public Documento(AtualizaDocumentoDTO documentoDTO, Beneficiario beneficiario) {
        this.tipoDocumento = documentoDTO.getTipoDocumento();
        this.descricao = documentoDTO.getDescricao();
        this.dataInclusao = LocalDateTime.now();
        this.beneficiario = beneficiario;
    }

    public DocumentoDTO toDTO() {
        DocumentoDTO dto = new DocumentoDTO();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }

    public void update(AtualizaDocumentoDTO dto) {
        this.tipoDocumento = hasText(dto.getTipoDocumento()) ? dto.getTipoDocumento() : this.tipoDocumento;
        this.descricao = hasText(dto.getDescricao()) ? dto.getDescricao() : this.descricao;
        this.dataAtualizacao = LocalDateTime.now();
    }
}
