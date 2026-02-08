package io.github.raphaelmun1z.ecommerce.payment.dto;

public record Product(
        String externalId,
        String name,
        String description,
        Integer quantity,
        Integer price // Em centavos (ex: 100 = R$ 1,00)
) {}