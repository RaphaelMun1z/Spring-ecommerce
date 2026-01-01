package io.github.raphaelmun1z.ecommerce.entities.autorizacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_permissao")
@Schema(description = "Entidade que representa uma Permissão (Authority) no sistema de segurança")
public class Permissao implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da permissão", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @NotBlank(message = "O nome da permissão é obrigatório.")
    @Column(nullable = false, unique = true)
    @Schema(description = "Nome da permissão (ex: ADMIN, USER_READ, PRODUCT_WRITE)", example = "ADMIN")
    private String nome;

    @Override
    @Schema(hidden = true)
    public String getAuthority() {
        return this.nome;
    }
}