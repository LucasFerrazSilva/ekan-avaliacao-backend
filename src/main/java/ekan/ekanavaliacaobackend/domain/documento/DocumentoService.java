package ekan.ekanavaliacaobackend.domain.documento;

import ekan.ekanavaliacaobackend.domain.beneficiario.Beneficiario;
import org.springframework.stereotype.Service;

@Service
public class DocumentoService {

    private DocumentoRepository repository;

    public DocumentoService(DocumentoRepository repository) {
        this.repository = repository;
    }

    public void listAllByBeneficiario(Beneficiario beneficiario) {}

}
