package io.github.raphaelmun1z.ecommerce.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AbacatePayResponse(
        boolean success,
        String error,
        Data data
) {
    public record Data(
            @JsonProperty("_id") String id,
            String url,
            String status,
            Billing customer
    ) {}

    public record Billing(
            @JsonProperty("_id") String id,
            String name
    ) {}
}