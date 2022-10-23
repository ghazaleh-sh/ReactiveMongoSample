package com.reactive.template.demo.controlles;

import com.reactive.template.demo.model.Kayak;
import com.reactive.template.demo.repositories.KayakRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}
