package com.dtits.springwebfluxrest.bootstrap;

import com.dtits.springwebfluxrest.domains.Category;
import com.dtits.springwebfluxrest.domains.Vendor;
import com.dtits.springwebfluxrest.repositories.CategoryRepository;
import com.dtits.springwebfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (Objects.requireNonNull(categoryRepository.count().block()).intValue() == 0) {
            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Nuts").build()).block();
            categoryRepository.save(Category.builder().description("Breads").build()).block();
            categoryRepository.save(Category.builder().description("Meats").build()).block();
            categoryRepository.save(Category.builder().description("Eggs").build()).block();
        }

        if (Objects.requireNonNull(vendorRepository.count().block()).intValue() == 0) {
            vendorRepository.save(Vendor.builder().firstName("Joe").lastName("Buck").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Michael").lastName("Weston").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Jane").lastName("Doe").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Bill").lastName("Hamilton").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Jimmy").lastName("Buffet").build()).block();
        }
    }
}
