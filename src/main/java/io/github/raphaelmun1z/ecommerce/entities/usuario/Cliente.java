package io.github.raphaelmun1z.ecommerce.entities.usuario;

import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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
public class Cliente extends Usuario implements Serializable {

    public Cliente(String nome, String email, String senha, Papel papel) {
        super(nome, email, senha, papel);
    }
}