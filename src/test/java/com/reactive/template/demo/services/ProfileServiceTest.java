package com.reactive.template.demo.services;

import com.reactive.template.demo.model.Profile;
import com.reactive.template.demo.repositories.ProfileRepository;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.function.Predicate;

/**
 * @DataMongoTest for he Spring Boot test slice for MongoDB testing
 *
 */
@Log4j2
@DataMongoTest
@Import(ProfileService.class)
class ProfileServiceTest {

    @Autowired
    private ProfileService service;
    @Autowired
    private ProfileRepository repository;

    @Test
    void getAll() {
        Flux<Profile> saved = repository.saveAll(Flux.just(new Profile(null, "Josh"),
                                                           new Profile(null, "Matt"),
                                                           new Profile(null, "Jane")));

        Flux<Profile> composite = service.all().thenMany(saved);

        Predicate<Profile> match = profile -> saved.any(saveItem -> saveItem.equals(profile)).block();

        //equivalent to Assert
        //The StepVerifier is central to testing all things reactive.
        //It gives us a way to assert that what we think is going to come next in the publisher,
        // is in fact going to come next in the publisher.
        StepVerifier
                .create(composite)//In this unit test we setup state in one publisher (saved).
                .expectNextMatches(match)//…​and then assert things about that state in the various expectNextMatches calls
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();//Make sure to call verifyComplete! Otherwise, nothing will happen…​ and that makes me sad.

    }

    @Test
    void save() {
        Mono<Profile> profileMono = service.create("email@email.com");
        StepVerifier
                .create(profileMono)
                .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                .verifyComplete();
    }

    @Test
    void update() {
        Mono<Profile> saved = service
                .create("test")
                .flatMap(p -> service.update(p.getId(), "test1"));
        StepVerifier
                .create(saved)
                .expectNextMatches(p -> p.getEmail().equalsIgnoreCase("test1"))
                .verifyComplete();
    }

    @Test
    void delete() {
        String test = "test";
        Mono<Profile> deleted = service
                .create(test)
                .flatMap(saved -> service.delete(saved.getId()));
        StepVerifier
                .create(deleted)
                .expectNextMatches(profile -> profile.getEmail().equalsIgnoreCase(test))
                .verifyComplete();
    }

    @Test
    void getById() {
        String test = UUID.randomUUID().toString();
        Mono<Profile> created = service
                .create(test)
                .flatMap(saved -> service.get(saved.getId()));
        StepVerifier
                .create(created)
                .expectNextMatches(profile -> StringUtils.hasText(profile.getId()) && test.equalsIgnoreCase(profile.getEmail()))
                .verifyComplete();
    }
}