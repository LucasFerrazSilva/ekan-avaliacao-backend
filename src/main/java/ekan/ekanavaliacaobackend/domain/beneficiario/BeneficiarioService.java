package ekan.ekanavaliacaobackend.domain.beneficiario;

import org.springframework.stereotype.Service;

@Service
public class BeneficiarioService {

    private BeneficiarioRepository repository;

    public BeneficiarioService(BeneficiarioRepository repository) {
        this.repository = repository;
    }

    public void list() {}

    public void get(Long id) {}

    public void create(NovoBeneficiarioDTO dto) {}

    public void update(BeneficiarioDTO dto) {}

    public void delete(Long id) {}

}
