package com.reactive.template.demo.services;

import com.reactive.template.demo.configs.ProfileCreatedEvent;
import com.reactive.template.demo.model.Profile;
import com.reactive.template.demo.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProfileService {

    /**
     * 	we’ll want to publish events to other beans managed in the Spring ApplicationContext.
     * 	Earlier, we defined an ApplicationListener<ApplicationReadyEvent>
     * 	    that listened for an event that was published in the ApplicationContext.
     * 	Now, we’re going to publish an event for consumption of other beans of our devices in the ApplicationContext.
     */
    private final ApplicationEventPublisher publisher;

    private final ProfileRepository profileRepository;


    public Flux<Profile> all() {
        return profileRepository.findAll();
    }

    public Mono<Profile> get(String id) {
        return profileRepository.findById(id);
    }

    public Mono<Profile> update(String id, String email) {
        return profileRepository
                .findById(id)
                .map(p -> new Profile(p.getId(), email))
                .flatMap(this.profileRepository::save);
    }

    public Mono<Profile> delete(String id) {
        return profileRepository
                .findById(id)
                .flatMap(p -> this.profileRepository.deleteById(p.getId()).thenReturn(p));
    }

    public Mono<Profile> create(String email) {
        return profileRepository
                .save(new Profile(null, email))
                .doOnSuccess(profile -> publisher.publishEvent(new ProfileCreatedEvent(profile)));
    }
}
