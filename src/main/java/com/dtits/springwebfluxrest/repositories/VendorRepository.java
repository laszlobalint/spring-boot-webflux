package com.dtits.springwebfluxrest.repositories;

import com.dtits.springwebfluxrest.domains.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
