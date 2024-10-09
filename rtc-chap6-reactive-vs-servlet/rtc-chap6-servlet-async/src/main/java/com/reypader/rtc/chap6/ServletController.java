package com.reypader.rtc.chap6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ServletController {
    private final Random randomizer = new Random();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @GetMapping("/instant")
    public String instant() {
        return "INSTANT";
    }

    @GetMapping("/delayed")
    public CompletableFuture<String> delayed() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        executor.submit(() -> {
            Thread.sleep(randomizer.nextInt(100, 10000));
            completableFuture.complete("DELAYED");
            return null;
        });
        return completableFuture;
    }
}
