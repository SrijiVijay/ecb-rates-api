package me.vitblokhin.ecbratesapi.controller;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.dto.filter.QueryFilter;
import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.exception.InvalidParameterException;
import me.vitblokhin.ecbratesapi.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Log4j2
@RestController
public class MainController {
    private final RateService rateService;

    @Autowired
    public MainController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<DailyRateDto> getDaily(QueryFilter filter) {
        return ResponseEntity.ok(rateService.getDailyRate(filter, LocalDate.now()));
    }

    @GetMapping(value = "/latest", produces = "application/json")
    public ResponseEntity<DailyRateDto> getLatest(QueryFilter filter) {
        return ResponseEntity.ok(rateService.getDailyRate(filter, LocalDate.now()));
    }

    @GetMapping(value = "/{date}", produces = "application/json")
    public ResponseEntity<DailyRateDto> getForDate(QueryFilter filter, @PathVariable("date") String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);

            return ResponseEntity.ok(rateService.getDailyRate(filter, date));
        } catch (DateTimeParseException ex) {
            throw new InvalidParameterException("Time string \'" + dateStr + "\' does not match format \'yyyy-MM-dd\'");
        }
    }
} // class MainController
