package io.github.raphaelmun1z.ecommerce.entities.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidade que representa um endereço salvo no perfil do cliente")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID do endereço", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;

    @NotBlank(message = "O apelido do endereço é obrigatório (ex: Casa, Trabalho).")
    @Schema(description = "Identificador amigável do endereço", example = "Minha Casa")
    private String apelido;

    @NotBlank
    @Size(max = 9)
    @Schema(description = "CEP", example = "01001-000")
    private String cep;

    @NotBlank
    @Schema(description = "Logradouro", example = "Av. Paulista")
    private String logradouro;

    @NotBlank
    @Schema(description = "Número", example = "1000")
    private String numero;

    @Schema(description = "Complemento", example = "Apto 101")
    private String complemento;

    @NotBlank
    @Schema(description = "Bairro", example = "Bela Vista")
    private String bairro;

    @NotBlank
    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;

    @NotBlank
    @Size(min = 2, max = 2)
    @Schema(description = "Estado (UF)", example = "SP")
    private String uf;

    @Schema(description = "Define se é o endereço padrão de entrega", example = "true")
    private boolean principal = false;
}