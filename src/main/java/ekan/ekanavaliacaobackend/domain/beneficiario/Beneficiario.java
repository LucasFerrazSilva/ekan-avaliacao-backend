package ekan.ekanavaliacaobackend.domain.beneficiario;

import ekan.ekanavaliacaobackend.domain.documento.AtualizaDocumentoDTO;
import ekan.ekanavaliacaobackend.domain.documento.Documento;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
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
            this.documentos = dto.getDocumentosDTOs().stream().map(documentoDTO -> new Documento(documentoDTO, this)).collect(toList());
    }

    public void update(AtualizaBeneficiarioDTO dto) {
        this.nome = hasText(dto.getNome()) ? dto.getNome() : this.nome;
        this.telefone = hasText(dto.getTelefone()) ? dto.getTelefone() : this.telefone;
        this.dataNascimento = dto.getDataNascimento() != null ? dto.getDataNascimento() : this.dataNascimento;
        this.dataAtualizacao = LocalDateTime.now();

        this.updateDocumentos(dto);
    }

    private void updateDocumentos(AtualizaBeneficiarioDTO dto) {
        List<AtualizaDocumentoDTO> documentosDTOs = dto.getDocumentosDTOs();

        if (documentosDTOs == null || documentosDTOs.isEmpty()) {
            this.documentos = null;
            return;
        }

        if (this.documentos == null || this.documentos.isEmpty())
            this.documentos = new ArrayList<>();
        else {
            List<Long> documentosIds = documentosDTOs.stream().filter(item -> item.getId() != null).map(AtualizaDocumentoDTO::getId).collect(toList());
            this.documentos = this.documentos.stream().filter(documento -> documentosIds.contains(documento.getId())).collect(toList());
        }

        documentosDTOs.forEach(documentoDTO -> {
            if (documentoDTO.getId() != null) {
                this.documentos.stream().filter(item -> documentoDTO.getId().equals(item.getId())).findAny().ifPresent(item -> item.update(documentoDTO));
            } else {
                this.documentos.add(new Documento(documentoDTO, this));
            }
        });
    }

    public BeneficiarioDTO toDTO() {
        var dto = new BeneficiarioDTO();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }

}
