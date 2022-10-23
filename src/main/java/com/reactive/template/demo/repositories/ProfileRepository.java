package com.reactive.template.demo.repositories;

import com.reactive.template.demo.model.Profile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
    Flux<Profile> findByEmail(String email);
}

