package me.vitblokhin.ecbratesapi.controller;

import me.vitblokhin.ecbratesapi.client.RateClient;
import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.dto.json.TimeSeriesRateDto;
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

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<DailyRateDto> getDaily(){
        return ResponseEntity.ok(rateClient.fetchDailyRates());
    }

    @GetMapping(value = "/historical", produces = "application/json")
    public ResponseEntity<TimeSeriesRateDto> getHistorical(){
        return ResponseEntity.ok(rateClient.fetchHistoricalRates());
    }
} // class MainController
