package ekan.ekanavaliacaobackend.domain.beneficiario;

import ekan.ekanavaliacaobackend.domain.documento.Documento;
import ekan.ekanavaliacaobackend.domain.documento.DocumentoDTO;
import ekan.ekanavaliacaobackend.domain.documento.DocumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class BeneficiarioService {

    private BeneficiarioRepository repository;
    private DocumentoRepository documentoRepository;

    public BeneficiarioService(BeneficiarioRepository repository, DocumentoRepository documentoRepository) {
        this.repository = repository;
        this.documentoRepository = documentoRepository;
    }

    public List<BeneficiarioDTO> list() {
        var list = repository.findAll();
        var dtoList = list.stream().map(Beneficiario::toDTO).collect(toList());
        return dtoList;
    }

    public BeneficiarioDTO get(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new).toDTO();
    }

    public List<DocumentoDTO> getDocuments(Long id) {
        return documentoRepository.findByBeneficiarioId(id).stream().map(Documento::toDTO).collect(toList());
    }

    @Transactional
    public BeneficiarioDTO create(NovoBeneficiarioDTO dto) {
        var beneficiario = new Beneficiario(dto);
        return repository.save(beneficiario).toDTO();
    }

    @Transactional
    public BeneficiarioDTO update(BeneficiarioDTO dto) {
        var beneficiario = repository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        beneficiario.update(dto);
        return beneficiario.toDTO();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
