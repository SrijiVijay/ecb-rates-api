package me.vitblokhin.ecbratesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcbRatesApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcbRatesApplication.class, args);
    }
} // class EcbRatesApplication
