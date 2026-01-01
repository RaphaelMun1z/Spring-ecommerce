package io.github.raphaelmun1z.ecommerce.exceptions.models;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String msg) {
        super(msg);
    }
}