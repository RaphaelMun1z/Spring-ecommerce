package io.github.raphaelmun1z.ecommerce.payment.dto;

public record Customer(
        String name,
        String cellphone,
        String email,
        String taxId // CPF ou CNPJ
) {}