package com.reypader.rtc.chap6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ServletController {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @GetMapping("/instant")
    public CompletableFuture<String> instant() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        executor.submit(() -> {
            completableFuture.complete("INSTANT");
            return null;
        });
        return completableFuture;
    }

    @GetMapping("/delayed")
    public CompletableFuture<String> delayed() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        executor.submit(() -> {
            Thread.sleep(10000);
            completableFuture.complete("DELAYED");
            return null;
        });
        return completableFuture;
    }
}
