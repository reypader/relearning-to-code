package com.reypader.rtc.chap6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class ReactiveController {

    @GetMapping("/instant")
    public Mono<String> instant() {
        return Mono.just("INSTANT");
    }

    @GetMapping("/delayed")
    public Mono<String> delayed() {
        return Mono.delay(Duration.ofMillis(10000)).thenReturn("DELAYED");
    }
}
