package me.vitblokhin.ecbratesapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RateClientConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
} // class RateClientConfig
