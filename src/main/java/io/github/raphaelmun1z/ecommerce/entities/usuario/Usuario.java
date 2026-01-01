package io.github.raphaelmun1z.ecommerce.entities.usuario;

import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"papel"})
@EqualsAndHashCode(of = "id")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_usuario", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Schema(description = "Entidade abstrata base para usuários do sistema (Clientes e Administradores)")
public abstract class Usuario implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @NotBlank(message = "O nome não pode ser vazio ou nulo.")
    @Column(nullable = false)
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    private String nome;

    @NotBlank(message = "O e-mail não pode ser vazio ou nulo.")
    @Email(message = "O formato do e-mail é inválido.")
    @Column(nullable = false, unique = true)
    @Schema(description = "Endereço de e-mail para login e contato", example = "joao@email.com")
    private String email;

    @NotBlank(message = "A senha não pode ser vazia ou nula.")
    @Column(nullable = false)
    @Schema(description = "Senha criptografada do usuário", hidden = true)
    private String senha;

    @Schema(hidden = true)
    private String passwordResetToken;

    @Schema(hidden = true)
    private LocalDateTime passwordResetTokenExpiry;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Schema(description = "Data e hora do cadastro do usuário")
    private LocalDateTime dataCadastro;

    @NotNull(message = "O papel do usuário não pode ser nulo.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "papel_id", nullable = false)
    @Schema(description = "Papel (Role) de acesso atribuído ao usuário")
    private Papel papel;

    @Schema(description = "Indica se a conta do usuário não expirou")
    private boolean accountNonExpired = true;

    @Schema(description = "Indica se a conta do usuário não está bloqueada")
    private boolean accountNonLocked = true;

    @Schema(description = "Indica se as credenciais do usuário não expiraram")
    private boolean credentialsNonExpired = true;

    @Schema(description = "Indica se o usuário está ativo")
    private boolean enabled = true;

    public Usuario(String nome, String email, String senha, Papel papel) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.papel = papel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (this.papel != null) {
            authorities.add(new SimpleGrantedAuthority(this.papel.getNome()));
            authorities.addAll(this.papel.getPermissoes());
        }
        return authorities;
    }

    public boolean hasRole(String nomeDoPapel) {
        if (this.papel == null || nomeDoPapel == null) {
            return false;
        }
        return this.papel.getNome().equalsIgnoreCase(nomeDoPapel);
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}