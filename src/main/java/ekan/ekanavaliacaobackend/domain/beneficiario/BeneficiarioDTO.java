package ekan.ekanavaliacaobackend.domain.beneficiario;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @AllArgsConstructor
public class BeneficiarioDTO {

    private Long id;
    private String nome;
    private String telefone;
    private LocalDate dataNascimento;
    private LocalDateTime dataInclusao;
    private LocalDateTime dataAtualizacao;

}
