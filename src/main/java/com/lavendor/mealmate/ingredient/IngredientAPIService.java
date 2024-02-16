package com.lavendor.mealmate.ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class IngredientAPIService {

    private final WebClient webClient;

    @Autowired
    public IngredientAPIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.apilayer.com/spoonacular").build();
    }

    public IngredientAPI getRecipeInformation(int recipeId, String apiKey) {
        return webClient.get()
                .uri("/recipes/{id}/information?apiKey={apiKey}", recipeId, apiKey)
                .retrieve()
                .bodyToMono(IngredientAPI.class)
                .block();
    }
    public List<IngredientAPI> searchIngredients(String query, String apiKey) {
        return webClient.get()
                .uri("/food/ingredients/search?query={query}", query)
                .header(HttpHeaders.AUTHORIZATION, "apiKey " + apiKey)
                .retrieve()
                .bodyToFlux(IngredientAPI.class)
                .collectList()
                .block();
    }
}
