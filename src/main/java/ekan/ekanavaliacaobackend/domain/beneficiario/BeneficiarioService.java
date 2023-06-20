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

    public BeneficiarioComDocumentosDTO get(Long id) {
        return new BeneficiarioComDocumentosDTO(this.findById(id));
    }

    public List<DocumentoDTO> getDocuments(Long id) {
        this.findById(id);
        var documentos = documentoRepository.findByBeneficiarioId(id);
        var list = documentos.stream().map(Documento::toDTO).collect(toList());
        return list;
    }

    @Transactional
    public BeneficiarioDTO create(NovoBeneficiarioDTO dto) {
        var beneficiario = new Beneficiario(dto);
        return repository.save(beneficiario).toDTO();
    }

    @Transactional
    public BeneficiarioComDocumentosDTO update(AtualizaBeneficiarioDTO dto) {
        var beneficiario = this.findById(dto.getId());
        beneficiario.update(dto);
        return new BeneficiarioComDocumentosDTO(beneficiario);
    }

    @Transactional
    public void delete(Long id) {
        Beneficiario beneficiario = this.findById(id);
        repository.delete(beneficiario);
    }

    private Beneficiario findById(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
