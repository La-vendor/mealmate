package com.lavendor.mealmate.ingredient;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class ApiConfig {

    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
