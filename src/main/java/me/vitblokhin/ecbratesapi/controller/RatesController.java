package me.vitblokhin.ecbratesapi.controller;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.dto.filter.BasicFilter;
import me.vitblokhin.ecbratesapi.dto.filter.HistoricalFilter;
import me.vitblokhin.ecbratesapi.dto.filter.SingleDateFilter;
import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.dto.json.ExceptionDto;
import me.vitblokhin.ecbratesapi.dto.json.HistoricalRateDto;
import me.vitblokhin.ecbratesapi.exception.InvalidParameterException;
import me.vitblokhin.ecbratesapi.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api")
public class RatesController {
    private final RateService rateService;

    @Autowired
    public RatesController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping(value = {"", "/latest"}, produces = "application/json")
    public ResponseEntity<DailyRateDto> getDaily(@Valid BasicFilter filter) {
        SingleDateFilter newFilter = new SingleDateFilter(filter, LocalDate.now());
        return ResponseEntity.ok(rateService.getDailyRate(newFilter));
    }

    @GetMapping(value = "/{date}", produces = "application/json")
    public ResponseEntity<DailyRateDto> getForDate(@Valid BasicFilter filter, @PathVariable("date") String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);

            SingleDateFilter newFilter = new SingleDateFilter(filter, date);

            return ResponseEntity.ok(rateService.getDailyRate(newFilter));
        } catch (DateTimeParseException ex) {
            throw new InvalidParameterException("String \'" + dateStr + "\' does not match format \'yyyy-MM-dd\' or is not a correct date");
        }
    }

    // almost the same method as above
    @GetMapping(value = "/date", produces = "application/json")
    public ResponseEntity<DailyRateDto> getForDate(@Valid SingleDateFilter filter) {
        return ResponseEntity.ok(rateService.getDailyRate(filter));
    }

    @GetMapping(value = "/historical", produces = "application/json")
    public ResponseEntity<HistoricalRateDto> getHistorical(@Valid HistoricalFilter filter) {
        return ResponseEntity.ok(rateService.getHistoricalRate(filter));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionDto> handleBindException(HttpServletRequest req, BindException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ExceptionDto exceptionDto =
                new ExceptionDto("Invalid parameter(s)", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exceptionDto);
    }
} // class RatesController
