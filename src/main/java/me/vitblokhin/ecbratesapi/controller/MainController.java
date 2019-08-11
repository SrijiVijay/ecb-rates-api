package me.vitblokhin.ecbratesapi.controller;

import me.vitblokhin.ecbratesapi.client.RateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    private final RateClient rateClient;

    @Autowired
    public MainController(RateClient rateClient) {
        this.rateClient = rateClient;
    }

    @GetMapping("/")
    public ResponseEntity<String> getEnvelope(){
        return ResponseEntity.ok(rateClient.fetchDailyRates().toString());
    }
} // class MainController
