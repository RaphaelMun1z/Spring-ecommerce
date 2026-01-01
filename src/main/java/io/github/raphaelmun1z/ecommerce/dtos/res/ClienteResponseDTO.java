package io.github.raphaelmun1z.ecommerce.dtos.res;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;

public class ClienteResponseDTO {
    private String id;
    private String nome;
    private String email;
    private String avatar;

    public ClienteResponseDTO(Cliente entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.email = entity.getEmail();
        //this.avatar = entity.getAvatar();
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