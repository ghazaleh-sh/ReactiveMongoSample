package com.reactive.template.demo.repositories;

import com.reactive.template.demo.model.Kayak;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface KayakRepository extends ReactiveMongoRepository<Kayak, Long> {

    Flux<Kayak> findByName(String email);
//    Flux<Kayak> accountFlux = repository
//            .findAll(Example.of(new Kayak(null, "owner", null, null)));
}
