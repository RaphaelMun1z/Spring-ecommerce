package io.github.raphaelmun1z.ecommerce.entities.usuario;

import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_cliente")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Schema(description = "Entidade que representa um cliente da loja, com dados pessoais e histórico de compras")
public class Cliente extends Usuario implements Serializable {

    @NotBlank(message = "O CPF é obrigatório.")
    @Column(nullable = false, length = 14, unique = true)
    @Schema(description = "CPF do cliente (apenas números)", example = "12345678900")
    private String cpf;

    @Column(length = 20)
    @Schema(description = "Telefone de contato", example = "11999999999")
    private String telefone;

    public Cliente(String nome, String email, String senha, Papel papel, String cpf, String telefone) {
        super(nome, email, senha, papel);
        this.cpf = cpf;
        this.telefone = telefone;
    }
}