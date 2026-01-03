package io.github.raphaelmun1z.ecommerce.dtos.req.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro ou atualização de endereço")
public record EnderecoRequestDTO(
    @NotBlank(message = "O apelido é obrigatório.")
    @Schema(example = "Trabalho")
    String apelido,

    @NotBlank(message = "O CEP é obrigatório.")
    @Size(max = 9)
    @Schema(example = "20040-002")
    String cep,

    @NotBlank(message = "O logradouro é obrigatório.")
    @Schema(example = "Rua Rio Branco")
    String logradouro,

    @NotBlank(message = "O número é obrigatório.")
    @Schema(example = "156")
    String numero,

    @Schema(example = "Sala 502")
    String complemento,

    @NotBlank(message = "O bairro é obrigatório.")
    @Schema(example = "Centro")
    String bairro,

    @NotBlank(message = "A cidade é obrigatória.")
    @Schema(example = "Rio de Janeiro")
    String cidade,

    @NotBlank(message = "A UF é obrigatória.")
    @Size(min = 2, max = 2)
    @Schema(example = "RJ")
    String uf,

    @Schema(description = "Definir como endereço principal?", example = "false")
    boolean principal
) {
}