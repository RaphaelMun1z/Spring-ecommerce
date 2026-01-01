package io.github.raphaelmun1z.ecommerce.entities.autorizacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "tb_papel")
@Schema(description = "Entidade que representa um Papel (Role) no sistema de autorização")
public class Papel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único do papel", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @NotBlank(message = "O nome do papel é obrigatório.")
    @Column(nullable = false, unique = true)
    @Schema(description = "Nome do papel (ex: ROLE_ADMIN, ROLE_CLIENTE)", example = "ROLE_ADMIN")
    private String nome;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tb_papel_permissao",
        joinColumns = @JoinColumn(name = "papel_id"),
        inverseJoinColumns = @JoinColumn(name = "permissao_id"))
    @ToString.Exclude
    @Schema(description = "Conjunto de permissões associadas a este papel")
    private Set<Permissao> permissoes;
}