package io.github.raphaelmun1z.ecommerce.entities.usuario;

import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_admin")
@Schema(description = "Entidade que representa um administrador do sistema, com privilégios de gestão")
public class Admin extends Usuario implements Serializable {
    public Admin(String nome, String email, String senha, Papel papel) {
        super(nome, email, senha, papel);
    }
}