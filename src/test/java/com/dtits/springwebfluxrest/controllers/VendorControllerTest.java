package com.dtits.springwebfluxrest.controllers;

import com.dtits.springwebfluxrest.domains.Vendor;
import com.dtits.springwebfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class VendorControllerTest {

    private final String ID = "/123";

    VendorRepository vendorRepository;

    VendorController vendorController;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void testGetAll() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("John").lastName("Doe").build(), Vendor.builder().firstName("Jane").lastName("Don").build()));

        webTestClient
                .get().uri(VendorController.BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void testGetById() {
        given(vendorRepository.findById(ID))
                .willReturn(Mono.just(Vendor.builder().id("123").firstName("John").lastName("Doe").build()));

        webTestClient
                .get().uri(VendorController.BASE_URL + ID)
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void testCreate() {
        Vendor vendor = new Vendor("123", "New", "Vendor");

        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(vendor));

        webTestClient.post()
                .uri(VendorController.BASE_URL)
                .body(Flux.just(vendor), Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdate() {
        Mono<Vendor> mono = Mono.just(Vendor.builder().build());

        given(vendorRepository.save(ArgumentMatchers.any(Vendor.class)))
                .willReturn(mono);

        webTestClient.put()
                .uri(VendorController.BASE_URL + ID)
                .body(mono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void testPatchWithChanges() {
        Mono<Vendor> oldMono = Mono.just(Vendor.builder().firstName("John").lastName("Doe").build());
        Mono<Vendor> newMono = Mono.just(Vendor.builder().firstName("Jane").lastName("Don").build());

        given(vendorRepository.findById(anyString()))
                .willReturn(oldMono);

        given(vendorRepository.save(ArgumentMatchers.any(Vendor.class)))
                .willReturn(newMono);

        webTestClient.patch()
                .uri(VendorController.BASE_URL + ID)
                .body(newMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(ArgumentMatchers.any(Vendor.class));
    }

    @Test
    public void testPatchWithoutChanges() {
        Mono<Vendor> mono = Mono.just(Vendor.builder().firstName("John").lastName("Doe").build());

        given(vendorRepository.findById(anyString()))
                .willReturn(mono);

        given(vendorRepository.save(ArgumentMatchers.any(Vendor.class)))
                .willReturn(mono);

        webTestClient.patch()
                .uri(VendorController.BASE_URL + ID)
                .body(mono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, Mockito.never()).save(ArgumentMatchers.any());
    }
}