package ekan.ekanavaliacaobackend.domain.documento;

import ekan.ekanavaliacaobackend.domain.beneficiario.Beneficiario;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_BENEFICIARIOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of="id")
@ToString(exclude="beneficiario")
public class Documento {

    private Long id;
    private String tipoDocumento;
    private String descricao;
    private LocalDateTime dataInclusao;
    private LocalDateTime dataAtualizacao;
    @ManyToOne
    @JoinColumn(name="beneficiario_id")
    private Beneficiario beneficiario;

}
