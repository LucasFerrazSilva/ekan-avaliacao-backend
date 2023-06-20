package ekan.ekanavaliacaobackend.controller;

import ekan.ekanavaliacaobackend.domain.beneficiario.*;
import ekan.ekanavaliacaobackend.domain.documento.DocumentoDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/beneficiarios")
public class BeneficiarioController {

    private BeneficiarioService service;

    public BeneficiarioController(BeneficiarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity list() {
        List<BeneficiarioDTO> list = this.service.list();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        BeneficiarioComDocumentosDTO dto = service.get(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/documentos")
    @Transactional
    public ResponseEntity getDocumentos(@PathVariable Long id) {
        List<DocumentoDTO> dto = service.getDocuments(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid NovoBeneficiarioDTO dto, UriComponentsBuilder uriComponentsBuilder) {
        BeneficiarioDTO beneficiarioDTO = service.create(dto);
        URI uri = uriComponentsBuilder.path("/beneficiarios/{id}").buildAndExpand(beneficiarioDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(beneficiarioDTO);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@RequestBody @Valid AtualizaBeneficiarioDTO dto) {
        BeneficiarioComDocumentosDTO beneficiarioDTO = service.update(dto);

        return ResponseEntity.ok(beneficiarioDTO);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
