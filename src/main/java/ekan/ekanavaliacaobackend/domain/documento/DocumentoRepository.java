package ekan.ekanavaliacaobackend.domain.documento;

import ekan.ekanavaliacaobackend.domain.beneficiario.Beneficiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findByBeneficiario(Beneficiario beneficiario);

}
