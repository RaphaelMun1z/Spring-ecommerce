package io.github.raphaelmun1z.ecommerce.dtos.res;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta com os dados públicos do cliente")
public class ClienteResponseDTO {

    @Schema(description = "Identificador único do cliente", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Nome completo do cliente", example = "Maria Silva")
    private String nome;

    @Schema(description = "E-mail de contato", example = "maria.silva@email.com")
    private String email;

    @Schema(description = "URL do avatar do perfil (se disponível)", example = "https://ui-avatars.com/api/?name=Maria+Silva")
    private String avatar;

    public ClienteResponseDTO(Cliente entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.email = entity.getEmail();
        // Lógica para avatar se existir na entidade ou for gerado
        // this.avatar = entity.getAvatar();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}