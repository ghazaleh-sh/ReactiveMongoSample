package com.reactive.template.demo.configs;

import com.reactive.template.demo.model.Kayak;
import com.reactive.template.demo.model.Profile;
import com.reactive.template.demo.repositories.KayakRepository;
import com.reactive.template.demo.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * to popular the database on startup.
 * Spring’s Profile annotation tags an object for initialization
 * only when the profile that matches the profile specified in the annotation is specifically activated.
 * <p>
 * about last line, Publisher<T> instances are lazy — you need to subscribe() to them to trigger their execution.
 */
@Log4j2
@Component
@RequiredArgsConstructor
@org.springframework.context.annotation.Profile("qa")
class SampleDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ProfileRepository profileRepository;
    private final KayakRepository kayakRepository;

//    public SampleDataInitializer(ProfileRepository profileRepository) {
//        this.profileRepository = profileRepository;
//    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        profileRepository
                .deleteAll()
                .thenMany(
                        Flux
                                .just("A", "B", "C", "D")
                                .map(name -> new Profile(UUID.randomUUID().toString(), name + "@email.com"))
                                .flatMap(profileRepository::save)
                )
                .thenMany(profileRepository.findAll())
                .subscribe(profile -> log.info("saving " + profile.toString()));


        Object[][] data = {
                {"sea", "Andrew", 300.12, "NDK"},
                {"creek", "Andrew", 100.75, "Piranha"},
                {"loaner", "Andrew", 75, "Necky"}
        };

        kayakRepository
                .deleteAll()
                .thenMany(
                        Flux
                                .just(data)
                                .map(array -> new Kayak((String) array[0], (String) array[1], (Number) array[2], (String) array[3]))
                                .flatMap(kayakRepository::save)
                )
                .thenMany(kayakRepository.findAll())
                .subscribe(kayak -> System.out.println("saving kayak " + kayak.toString()));

    }

}
