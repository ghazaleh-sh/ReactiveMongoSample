package com.reactive.template.demo.controlles;

import com.reactive.template.demo.model.Profile;
import com.reactive.template.demo.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * RestController tells Spring WebFlux that this class provides HTTP handler methods
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "${v1API}/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestController {

    private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
    private final ProfileService profileService;

    @GetMapping
    Publisher<Profile> getAll() {
        return profileService.all();
    }


    @GetMapping("/{id}")
    Publisher<Profile> getById(@PathVariable("id") String id) {
        return profileService.get(id);
    }


    @PostMapping
    Publisher<ResponseEntity<Profile>> create(@RequestBody Profile profile) {
        return profileService
                .create(profile.getEmail())
                .map(p -> ResponseEntity.created(URI.create("/profiles/" + p.getId()))
                        .contentType(mediaType)
                        .build());
    }

    @DeleteMapping("/{id}")
    Publisher<Profile> deleteById(@PathVariable String id) {
        return profileService.delete(id);
    }

    @PutMapping("/{id}")
    Publisher<ResponseEntity<Profile>> updateById(@PathVariable String id, @RequestBody Profile profile) {
        return Mono
                .just(profile)
                .flatMap(p -> profileService.update(id, p.getEmail()))
                .map(p -> ResponseEntity
                        .ok()
                        .contentType(mediaType)
                        .build());
    }
}
