package com.stopnrest.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();

            if(config.getExcludedEndpoints().contains(path)){

                return chain.filter(exchange);
            }

            boolean isExcludedHotels = path.matches("^/api/v1/hotels(/[^/]+)?$"); // Matches /api/v1/hotels and /api/v1/hotels/{anything}

            if (isExcludedHotels) {

                return chain.filter(exchange);
            }

            boolean isExcludedHotelsSearch = path.matches("^/api/v1/hotels/search/.*"); // Matches /api/v1/hotels and /api/v1/hotels/{anything}

            if (isExcludedHotelsSearch) {

                return chain.filter(exchange);
            }

            boolean isExcludedUsers = path.matches("^/api/v1/users"); // Matches /api/v1/hotels and /api/v1/hotels/{anything}

            if (isExcludedUsers) {

                return chain.filter(exchange);
            }

            boolean isExcludedRooms = path.matches("^/api/v1/rooms(/[^/]+)?$");
            if (isExcludedRooms) {

                return chain.filter(exchange);
            }

            boolean isExcludedRoomTypes = path.matches("^/api/v1/roomtypes(/[^/]+)?$");
            if (isExcludedRoomTypes) {

                return chain.filter(exchange);
            }

            boolean isExcludedHotelRooms = path.matches("^/api/v1/rooms/hotel/.*$");
            if (isExcludedHotelRooms) {

                return chain.filter(exchange);
            }

            boolean isExcludedHotelRoomTypes = path.matches("^/api/v1/roomtypes/hotel/.*$");
            if (isExcludedHotelRoomTypes) {
                return chain.filter(exchange);
            }



            if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = exchange.getRequest().getHeaders().get("Authorization").get(0);
            String[] parts = authHeader.split(" ");
            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                return this.onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = parts[1];
            return webClientBuilder.build()
                    .post()
                    .uri("http://localhost:8082/api/v1/auth/admins/validate")  // Auth Service validation endpoint
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnSuccess(response -> System.out.println("Token validated successfully"))
                    .then(chain.filter(exchange))
                    .onErrorResume(e -> {
                        System.out.println("Authentication failed: " + e.getMessage());
                        return this.onError(exchange, "Unauthorized", HttpStatus.UNAUTHORIZED);
                    });
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String errorResponse = "{\"message\": \"You are not authorized to access this Resource\"}";
        byte[] bytes = errorResponse.getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    public static class Config {
        private List<String> excludedEndpoints;

        public List<String> getExcludedEndpoints() {
            return excludedEndpoints;
        }

        public void setExcludedEndpoints(List<String> excludedEndpoints) {
            this.excludedEndpoints = excludedEndpoints;
        }
    }
}

