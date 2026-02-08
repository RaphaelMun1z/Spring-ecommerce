package io.github.raphaelmun1z.ecommerce.payment.dto;

import java.util.List;

public record CreateBillingRequest(
        String frequency,
        List<String> methods,
        List<Product> products,
        String returnUrl,
        String completionUrl,
        Customer customer
) {}