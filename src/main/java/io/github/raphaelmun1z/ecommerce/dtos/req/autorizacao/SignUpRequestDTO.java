package io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        @Email(message = "O email fornecido é inválido.")
        @NotBlank(message = "O email é obrigatório.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String senha
) { }