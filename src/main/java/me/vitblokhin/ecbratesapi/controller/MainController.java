package me.vitblokhin.ecbratesapi.controller;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.dto.filter.QueryFilter;
import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Log4j2
@RestController
public class MainController {
    //private final RateClient rateClient;
    private final RateService rateService;

    @Autowired
    public MainController( RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<DailyRateDto> getDaily(QueryFilter filter){
        log.info(filter);
        return ResponseEntity.ok(rateService.getDailyRate(filter, LocalDate.now()));
    }

//    @GetMapping(value = "/historical", produces = "application/json")
//    public ResponseEntity<TimeSeriesRateDto> getHistorical(){
//        return ResponseEntity.ok(rateClient.fetchHistoricalRates());
//    }
} // class MainController
