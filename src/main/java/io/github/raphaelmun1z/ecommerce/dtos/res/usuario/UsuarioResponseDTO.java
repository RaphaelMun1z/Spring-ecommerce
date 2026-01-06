package io.github.raphaelmun1z.ecommerce.dtos.res.usuario;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Usuario;

public record UsuarioResponseDTO(
    String id,
    String nome,
    String email,
    String avatar,
    String role,
    String tipo
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getAvatar(),
            usuario.getPapel() != null ? usuario.getPapel().getNome() : null,
            usuario.getClass().getSimpleName()
        );
    }
}