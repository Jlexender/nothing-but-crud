package ru.lexender.icarusdb.struct.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.struct.core.dto.StructRequest;
import ru.lexender.icarusdb.struct.core.dto.StructResponse;
import ru.lexender.icarusdb.struct.core.model.Struct;
import ru.lexender.icarusdb.struct.core.service.StructService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StructControllerTests {

    @Mock
    private StructService structService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private StructController structController;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(structController).build();
    }

    @Test
    public void findAllStructs_shouldFetchAllStructs() {
        Struct struct = new Struct();
        StructResponse structResponse = new StructResponse();

        when(structService.findAll()).thenReturn(Flux.just(struct));
        when(objectMapper.convertValue(struct, StructResponse.class)).thenReturn(structResponse);

        webTestClient.get()
                .uri("/api/v1/struct")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StructResponse.class);
    }

    @Test
    public void deleteAllStructs_shouldDeleteAllStructs() {
        when(structService.deleteAll()).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/struct")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void createStruct_shouldNotCreateStruct() {
        UUID id = UUID.randomUUID();
        StructRequest structRequest = new StructRequest();
        Struct struct = new Struct();
        StructResponse structResponse = new StructResponse();

        when(objectMapper.convertValue(structRequest, Struct.class)).thenReturn(struct);
        when(structService.save(any(Struct.class))).thenReturn(Mono.just(struct));
        when(objectMapper.convertValue(struct, StructResponse.class)).thenReturn(structResponse);

        webTestClient.post()
                .uri("/api/v1/struct")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LinkedHashMap<>())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void createStruct_shouldCreateStruct() {
        UUID id = UUID.randomUUID();
        String name = "name";
        Timestamp date = new Timestamp(new Date().getTime());
        StructRequest structRequest = new StructRequest(name, new LinkedHashMap<>());
        Struct struct = new Struct(id, name, new LinkedHashMap<>(), date);
        StructResponse structResponse = new StructResponse();

        when(objectMapper.convertValue(structRequest, Struct.class)).thenReturn(struct);
        when(structService.save(any(Struct.class))).thenReturn(Mono.just(struct));
        when(objectMapper.convertValue(struct, StructResponse.class)).thenReturn(structResponse);

        webTestClient.post()
                .uri("/api/v1/struct")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(structRequest)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void findStructById_shouldFetchStructById() {
        UUID id = UUID.randomUUID();
        Struct struct = new Struct();
        StructResponse structResponse = new StructResponse();

        when(structService.findById(id)).thenReturn(Mono.just(struct));
        when(objectMapper.convertValue(struct, StructResponse.class)).thenReturn(structResponse);

        webTestClient.get()
                .uri("/api/v1/struct/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StructResponse.class);
    }

    @Test
    public void findStructById_shouldReturnNotFound() {
        UUID id = UUID.randomUUID();

        when(structService.findById(id)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/struct/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void findStructById_shouldReturnBadRequest() {
        webTestClient.get()
                .uri("/api/v1/struct/ZOV")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void deleteStructById_shouldDeleteStructById() {
        UUID id = UUID.randomUUID();

        when(structService.deleteById(id)).thenReturn(Mono.empty());
        when(structService.existsById(id)).thenReturn(Mono.just(true));


        webTestClient.delete()
                .uri("/api/v1/struct/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void deleteStructById_shouldReturnNotFound() {
        UUID id = UUID.randomUUID();

        when(structService.deleteById(id)).thenReturn(Mono.<Void>empty());
        when(structService.existsById(id)).thenReturn(Mono.just(false));

        webTestClient.delete()
                .uri("/api/v1/struct/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void deleteStructById_shouldReturnBadRequest() {

        webTestClient.delete()
                .uri("/api/v1/struct/ZOV")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void countStructs_shouldCountAllStructs() {
        when(structService.count()).thenReturn(Mono.just(1L));

        webTestClient.get()
                .uri("/api/v1/struct/count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class);
    }
}