package ekan.ekanavaliacaobackend.domain.beneficiario;

import ekan.ekanavaliacaobackend.domain.documento.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@SpringBootTest
class BeneficiarioServiceTest {

    @Autowired
    private BeneficiarioService service;
    @Autowired
    private BeneficiarioRepository repository;
    @Autowired
    private DocumentoRepository documentoRepository;
    private Beneficiario beneficiario;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
        this.beneficiario = createBeneficiario();
    }

    private Beneficiario createBeneficiario() {
        List<NovoDocumentoDTO> documentos = getDocumentosDTOs();
        var dataNascimento = LocalDate.of(2000, 02, 01);
        var beneficiarioDTO = new NovoBeneficiarioDTO("Nome Beneficiario", "(11) 91234-5678", dataNascimento, documentos);
        var beneficiario = new Beneficiario(beneficiarioDTO);
        return repository.save(beneficiario);
    }

    private List<NovoDocumentoDTO> getDocumentosDTOs() {
        var documentoDTO = new NovoDocumentoDTO("pdf", "Dados do beneficiario");
        var documento2DTO = new NovoDocumentoDTO("txt", "Dados em texto");
        var documentos = List.of(documentoDTO, documento2DTO);
        return documentos;
    }

    @Test
    void testList() {
        // Given
        var beneficiario2 = createBeneficiario();
        var beneficiariosDTOList = List.of(beneficiario.toDTO(), beneficiario2.toDTO());

        // When
        var list = service.list();

        // Then
        assertThat(list).isNotEmpty();
        assertThat(list).hasSize(2);
        assertThat(list).hasSameElementsAs(beneficiariosDTOList);
    }

    @Test
    void testListWithNoElements() {
        // Given
        this.repository.deleteAll();

        // When
        var list = service.list();

        // Then
        assertThat(list).isEmpty();
    }

    @Test
    @Transactional
    void testGet() {
        // Given
        Long id = beneficiario.getId();

        // When
        BeneficiarioComDocumentosDTO beneficiario1 = service.get(id);

        // Then
        assertThat(beneficiario1).isEqualTo(new BeneficiarioComDocumentosDTO(beneficiario));
        assertThat(beneficiario1);
    }

    @Test
    void testGetWithInvalidId() {
        // Given
        Long invalidId = 123l;

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> service.get(invalidId));
    }

    @Test
    @Transactional
    void testGetDocuments() {
        // Given
        Long id = beneficiario.getId();
        var expectedDocuments = beneficiario.getDocumentos().stream().map(Documento::toDTO).collect(toList());

        // When
        List<DocumentoDTO> documents = service.getDocuments(id);

        // Then
        assertThat(documents).isNotEmpty();
        assertThat(documents).hasSize(2);
        assertThat(documents).hasSameElementsAs(expectedDocuments);
    }

    @Test
    @Transactional
    void testCreate() {
        // Given
        var documentosDTOs = getDocumentosDTOs();
        var dto = new NovoBeneficiarioDTO("Beneficiario", "(11)99999-9999", LocalDate.of(1990, 7, 5), documentosDTOs);

        // When
        var beneficiarioDTO = service.create(dto);

        // Then
        assertThat(beneficiarioDTO).isNotNull();
        assertThat(beneficiarioDTO.getNome()).isEqualTo(dto.getNome());
        assertThat(beneficiarioDTO.getTelefone()).isEqualTo(dto.getTelefone());
        assertThat(beneficiarioDTO.getDataNascimento()).isEqualTo(dto.getDataNascimento());
        assertThat(beneficiarioDTO.getDataInclusao()).isNotNull();
        assertThat(beneficiarioDTO.getDataAtualizacao()).isNull();

        var documentos = documentoRepository.findByBeneficiarioId(beneficiarioDTO.getId());
        assertThat(documentos).isNotEmpty();
        documentos.forEach(documento -> {
            assertThat(documento.getTipoDocumento()).isNotBlank();
            assertThat(documento.getDescricao()).isNotBlank();
            assertThat(documento.getBeneficiario()).isNotNull();
            assertThat(documento.getDataInclusao()).isNotNull();
            assertThat(documento.getDataAtualizacao()).isNull();
        });
    }

    @Test
    @Transactional
    void testUpdate() {
        // Given
        var nome = "Novo nome";
        var telefone = "(11) 99999-9999";
        var dataNascimento = LocalDate.of(1999, 12, 31);

        var novoTipoDocumento = "Novo tipo de documento";
        var novaDescricao = "Nova descrição";
        var documentosDTOs =
                beneficiario.getDocumentos().stream().map(
                        documento -> new AtualizaDocumentoDTO(documento.getId(), novoTipoDocumento, novaDescricao)
                ).collect(toList());
        var dto = new AtualizaBeneficiarioDTO(beneficiario.getId(), nome, telefone, dataNascimento, documentosDTOs);

        // When
        service.update(dto);
        var beneficiarioAtualizado = repository.findById(dto.getId()).get();

        // Then
        assertThat(beneficiarioAtualizado.getNome()).isEqualTo(dto.getNome());
        assertThat(beneficiarioAtualizado.getTelefone()).isEqualTo(dto.getTelefone());
        assertThat(beneficiarioAtualizado.getDataNascimento()).isEqualTo(dto.getDataNascimento());
        assertThat(beneficiarioAtualizado.getDataAtualizacao()).isNotNull();
        assertThat(beneficiarioAtualizado.getDocumentos()).isNotEmpty();
        var documento = beneficiarioAtualizado.getDocumentos().get(0);
        assertThat(documento.getTipoDocumento()).isEqualTo(novoTipoDocumento);
        assertThat(documento.getDescricao()).isEqualTo(novaDescricao);
        assertThat(documento.getDataAtualizacao()).isNotNull();
    }

    @Test
    @Transactional
    void testDelete() {
        // Given
        Long id = beneficiario.getId();

        // When
        service.delete(id);

        // Then
        assertThrows(NoSuchElementException.class, () -> repository.findById(id).get());
    }

}