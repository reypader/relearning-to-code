package com.reypader.rtc.chap6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class ServletController {

    @GetMapping("/instant")
    public String instant() {
        return "INSTANT";
    }

    @GetMapping("/delayed")
    public String delayed() throws Exception {
        Thread.sleep(10000);
        return "DELAYED";
    }
}
