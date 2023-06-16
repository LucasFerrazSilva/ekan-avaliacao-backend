package ekan.ekanavaliacaobackend.domain.beneficiario;

import ekan.ekanavaliacaobackend.domain.documento.Documento;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Entity
@Table(name="TB_BENEFICIARIOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of="id")
@ToString
public class Beneficiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String telefone;
    private LocalDate dataNascimento;
    private LocalDateTime dataInclusao;
    private LocalDateTime dataAtualizacao;
    @OneToMany(mappedBy = "beneficiario", cascade = CascadeType.ALL)
    private List<Documento> documentos;

    public Beneficiario(NovoBeneficiarioDTO dto) {
        this.nome = dto.getNome();
        this.telefone = dto.getTelefone();
        this.dataNascimento = dto.getDataNascimento();
        this.dataInclusao = LocalDateTime.now();

        if (dto.getDocumentosDTOs() != null)
            this.documentos = dto.getDocumentosDTOs().stream().map(documentoDTO -> new Documento(documentoDTO, this)).collect(Collectors.toList());
    }

    public void update(BeneficiarioDTO dto) {
        this.nome = hasText(dto.getNome()) ? dto.getNome() : this.nome;
        this.telefone = hasText(dto.getTelefone()) ? dto.getTelefone() : this.telefone;
        this.dataNascimento = dto.getDataNascimento() != null ? dto.getDataNascimento() : this.dataNascimento;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public BeneficiarioDTO toDTO() {
        var dto = new BeneficiarioDTO();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }

}
