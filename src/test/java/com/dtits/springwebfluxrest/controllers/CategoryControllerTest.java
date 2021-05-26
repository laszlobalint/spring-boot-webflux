package com.dtits.springwebfluxrest.controllers;

import com.dtits.springwebfluxrest.domains.Category;
import com.dtits.springwebfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CategoryControllerTest {

    private final String ID = "/123";

    CategoryRepository categoryRepository;

    CategoryController categoryController;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void testGetAll() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Fruit").build(), Category.builder().description("Grain").build()));

        webTestClient
                .get().uri(CategoryController.BASE_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void testGetById() {
        given(categoryRepository.findById(ID))
                .willReturn(Mono.just(Category.builder().description("Fruit").build()));

        webTestClient
                .get().uri(CategoryController.BASE_URL + ID)
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void testCreate() {
        Category category = new Category("123", "Fruits");

        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(category));

        webTestClient.post()
                .uri(CategoryController.BASE_URL)
                .body(Flux.just(category), Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdate() {
        Category category = new Category("123", "Fruits");

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        webTestClient.put()
                .uri(CategoryController.BASE_URL + ID)
                .body(Mono.just(category), Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void testPatchWithChanges() {
        Mono<Category> oldMono = Mono.just(Category.builder().description("Old Description").build());
        Mono<Category> newMono = Mono.just(Category.builder().description("New Description").build());

        given(categoryRepository.findById(anyString()))
                .willReturn(oldMono);

        given(categoryRepository.save(any(Category.class)))
                .willReturn(newMono);

        webTestClient.patch()
                .uri(CategoryController.BASE_URL + ID)
                .body(newMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testPatchWithNoChanges() {
        Mono<Category> mono = Mono.just(Category.builder().description("Description").build());

        given(categoryRepository.findById(anyString()))
                .willReturn(mono);

        given(categoryRepository.save(any(Category.class)))
                .willReturn(mono);

        webTestClient.patch()
                .uri(CategoryController.BASE_URL + ID)
                .body(mono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, never()).save(any());
    }
}