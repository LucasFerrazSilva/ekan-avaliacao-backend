package ekan.ekanavaliacaobackend.domain.beneficiario;

import ekan.ekanavaliacaobackend.domain.documento.Documento;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="TB_BENEFICIARIOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of="id")
@ToString
public class Beneficiario {

    private Long id;
    private String nome;
    private String telefone;
    private LocalDate dataNascimento;
    private LocalDateTime dataInclusao;
    private LocalDateTime dataAtualizacao;
    @OneToMany(mappedBy = "beneficiario")
    private List<Documento> documentos;

}
