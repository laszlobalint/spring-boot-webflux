package com.dtits.springwebfluxrest.repositories;

import com.dtits.springwebfluxrest.domains.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
