package io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requisição para iniciar o processo de recuperação de senha")
public record EsquecerSenhaRequestDTO(

    @Schema(description = "E-mail cadastrado do usuário que esqueceu a senha", example = "usuario@email.com")
    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "O formato do email é inválido.")
    String email
) {
}