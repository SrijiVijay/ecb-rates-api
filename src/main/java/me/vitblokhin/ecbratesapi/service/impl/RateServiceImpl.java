package me.vitblokhin.ecbratesapi.service.impl;

import me.vitblokhin.ecbratesapi.dto.filter.QueryFilter;
import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.exception.ItemNotFoundException;
import me.vitblokhin.ecbratesapi.model.ExchangeDate;
import me.vitblokhin.ecbratesapi.model.Rate;
import me.vitblokhin.ecbratesapi.repository.ExchangeDateRepository;
import me.vitblokhin.ecbratesapi.repository.RateRepository;
import me.vitblokhin.ecbratesapi.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("rateService")
public class RateServiceImpl implements RateService {

    private final RateRepository rateRepository;
    private final ExchangeDateRepository exchangeDateRepository;

    @Autowired
    public RateServiceImpl(RateRepository rateRepository,
                           ExchangeDateRepository exchangeDateRepository) {
        this.rateRepository = rateRepository;
        this.exchangeDateRepository = exchangeDateRepository;
    }

    @Override
    public DailyRateDto getDailyRate(QueryFilter filter, LocalDate date) {
        DailyRateDto dailyRates = new DailyRateDto();

        ExchangeDate exDate = exchangeDateRepository.findByDate(date)
                .orElse(exchangeDateRepository.findFirstByDateBeforeOrderByDateDesc(date)
                        .orElseThrow(() -> new ItemNotFoundException("Rates not found")));

        dailyRates.setDate(exDate.getDate());

        Map<String, BigDecimal> rateMap = new HashMap<>();
        String base = filter.getBase();
        List<String> symbols =
                (Objects.isNull(filter.getSymbols()) ||
                        filter.getSymbols().isEmpty()) ? null
                        : filter.getSymbols();

        if (base == null || "EUR".equals(base) || "".equals(base)) {
            dailyRates.setBase("EUR");
            for (Rate rate : exDate.getRates()) {
                String code = rate.getCurrency().getCharCode();
                if(Objects.isNull(symbols) || symbols.contains(code)) {
                    rateMap.put(code, rate.getRate());
                }
            }
        } else {
            Rate baseRate = exDate.getRates()
                    .stream()
                    .filter(r -> r.getCurrency()
                            .getCharCode()
                            .equals(base))
                    .findFirst()
                    .orElseThrow(() -> new ItemNotFoundException("Selected base not found"));

            dailyRates.setBase(baseRate.getCurrency().getCharCode());

            for (Rate rate : exDate.getRates()) {
                String code = rate.getCurrency().getCharCode();
                BigDecimal tmpRate = rate.getRate();
                if(code.equals(base)){
                    code = "EUR";
                    tmpRate = BigDecimal.valueOf(1);
                }
                if(Objects.isNull(symbols) || symbols.contains(code)) {
                    tmpRate = tmpRate.divide(baseRate.getRate(), rate.getRate().scale(), RoundingMode.HALF_UP);
                    rateMap.put(code, tmpRate);
                }
            }
        }

        dailyRates.setRates(rateMap);
        return dailyRates;
    }
} // class RateServiceImpl
