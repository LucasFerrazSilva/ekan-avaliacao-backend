package ekan.ekanavaliacaobackend.controller;

import ekan.ekanavaliacaobackend.domain.beneficiario.*;
import ekan.ekanavaliacaobackend.domain.documento.AtualizaDocumentoDTO;
import ekan.ekanavaliacaobackend.domain.documento.Documento;
import ekan.ekanavaliacaobackend.domain.documento.NovoDocumentoDTO;
import ekan.ekanavaliacaobackend.infra.exception.ValidationExceptionDataDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class BeneficiarioControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<List<BeneficiarioDTO>> beneficiarioDTOListJackson;
    @Autowired
    private JacksonTester<BeneficiarioDTO> beneficiarioDTOJackson;
    @Autowired
    private JacksonTester<BeneficiarioComDocumentosDTO> beneficiarioComDocumentosDTOJackson;
    @Autowired
    private JacksonTester<AtualizaBeneficiarioDTO> atualizaBeneficiarioDTOJackson;
    @Autowired
    private JacksonTester<NovoBeneficiarioDTO> novoBeneficiarioDTOJackson;
    @Autowired
    private JacksonTester<ValidationExceptionDataDTO> validationDTOJackson;
    @MockBean
    private BeneficiarioService service;

    @Test
    @WithMockUser
    void testList() throws Exception {
        // Given
        var beneficiario1DTO = createBeneficiario().toDTO();
        var beneficiario2DTO = new BeneficiarioDTO(1l, "Beneficiario2", "(11) 91234-5678", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        var dtoList = List.of(beneficiario1DTO, beneficiario2DTO);
        when(service.list()).thenReturn(dtoList);
        String expectedJson = beneficiarioDTOListJackson.write(dtoList).getJson();

        // When
        MockHttpServletResponse response = mvc.perform(get("/beneficiarios")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    @WithMockUser
    void testGet() throws Exception {
        // Given
        var dto = new BeneficiarioComDocumentosDTO(createBeneficiario());
        when(service.get(any())).thenReturn(dto);
        String expectedJson = beneficiarioComDocumentosDTOJackson.write(dto).getJson();

        // When
        MockHttpServletResponse response = mvc.perform(get("/beneficiarios/1")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    @WithMockUser
    void testCreate() throws Exception {
        // Given
        var beneficiario = createBeneficiario();
        var dto = beneficiario.toDTO();

        var documento = beneficiario.getDocumentos().get(0);
        var documentDTO = new NovoDocumentoDTO(documento.getTipoDocumento(), documento.getDescricao());
        var novoBeneficiarioDTO = new NovoBeneficiarioDTO(beneficiario.getNome(), beneficiario.getTelefone(), beneficiario.getDataNascimento(), List.of(documentDTO));

        String novoBeneficiarioDTOJson = novoBeneficiarioDTOJackson.write(novoBeneficiarioDTO).getJson();
        String expectedJson = beneficiarioDTOJackson.write(dto).getJson();

        when(service.create(any())).thenReturn(dto);

        // When
        MockHttpServletResponse response =
                mvc.perform(
                        post("/beneficiarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(novoBeneficiarioDTOJson)
                ).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("location")).contains("/beneficiarios/" + dto.getId());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    @WithMockUser
    void testInvalidCreate() throws Exception {
        // Given
        var beneficiario = createBeneficiario();
        var documento = beneficiario.getDocumentos().get(0);
        var documentDTO = new NovoDocumentoDTO(documento.getTipoDocumento(), null);
        var novoBeneficiarioDTO = new NovoBeneficiarioDTO(beneficiario.getNome(), null, beneficiario.getDataNascimento(), List.of(documentDTO));

        String novoBeneficiarioDTOJson = novoBeneficiarioDTOJackson.write(novoBeneficiarioDTO).getJson();

        String validation1Json = validationDTOJackson.write(new ValidationExceptionDataDTO("documentosDTOs[0].descricao", "must not be blank")).getJson();
        String validation2Json = validationDTOJackson.write(new ValidationExceptionDataDTO("telefone", "must not be blank")).getJson();

        // When
        MockHttpServletResponse response =
                mvc.perform(
                        post("/beneficiarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(novoBeneficiarioDTOJson)
                ).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains(validation1Json);
        assertThat(response.getContentAsString()).contains(validation2Json);
    }

    @Test
    @WithMockUser
    void testUpdate() throws Exception {
        // Given
        var beneficiario = createBeneficiario();
        var documentosDTOs =
                beneficiario.getDocumentos().stream().map(
                        documento -> new AtualizaDocumentoDTO(documento.getId(), documento.getTipoDocumento(), documento.getDescricao())
                ).collect(toList());
        AtualizaBeneficiarioDTO dto = new AtualizaBeneficiarioDTO(beneficiario.getId(), beneficiario.getNome(), beneficiario.getTelefone(), beneficiario.getDataNascimento(), documentosDTOs);
        var json = atualizaBeneficiarioDTOJackson.write(dto).getJson();

        var expectedDTO = new BeneficiarioComDocumentosDTO(beneficiario);
        String expectedJson = beneficiarioComDocumentosDTOJackson.write(expectedDTO).getJson();

        when(service.update(any())).thenReturn(expectedDTO);

        // When
        MockHttpServletResponse response =
                mvc.perform(
                        put("/beneficiarios/" + dto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    @WithMockUser
    void testDelete() throws Exception {
        // Given
        Long id = 1l;

        // When
        MockHttpServletResponse response = mvc.perform(delete("/beneficiarios/" + id)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @WithMockUser
    void testInvalidDelete() throws Exception {
        // Given
        Long id = 1l;
        doThrow(EntityNotFoundException.class).when(service).delete(any());

        // When
        MockHttpServletResponse response = mvc.perform(delete("/beneficiarios/" + id)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private Beneficiario createBeneficiario() {
        var documento = createDocumento();
        var beneficiario = new Beneficiario(1l, "Beneficiario", "(11) 91234-5678", LocalDate.now(), LocalDateTime.now(), null, List.of(documento));
        return beneficiario;
    }

    private Documento createDocumento() {
        return new Documento(1l, "RG", "RG numero x", LocalDateTime.now(), LocalDateTime.now(), null);
    }

}