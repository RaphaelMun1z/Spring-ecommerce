package io.github.raphaelmun1z.ecommerce.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private String localDateTime;
    private List<String> message;
    private String details;
}

