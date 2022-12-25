package com.reactive.template.demo.controlles;

import com.reactive.template.demo.model.Kayak;
import com.reactive.template.demo.repositories.KayakRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * This class looks an awful lot like a relational, blocking version.
 * A lot of behind the scenes work goes into making this the case. Have no fear,
 * however, this is 100% reactive, non-blocking code.
 */
@Controller
@RequestMapping(path = "/kayaks")
public class KayakController {

    private final KayakRepository kayakRepository;

    public KayakController(KayakRepository kayakRepository) {
        this.kayakRepository = kayakRepository;
    }

    @PostMapping()
    public @ResponseBody
    Mono<Kayak> addKayak(@RequestBody Kayak kayak) {
        return kayakRepository.save(kayak);
    }

    @GetMapping()
    public @ResponseBody
    Flux<Kayak> getAllKayaks() {
        return kayakRepository.findAll();
    }

    /**
     * A Server-Sent-Event is an HTTP standard that allows the client to receive updates whenever the server emits data,
     * basically it allows the client to receive real-time updates once the connection is established.
     * The client initiates the connection by requesting updates and keeps it open until the even stream is closed.
     *
     * @return Flux<Kayak>
     */
    @GetMapping(value = "/event", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Kayak> getUserEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .log()
                .map(val -> new Kayak("t", null, val, "test event"));
    }
}
