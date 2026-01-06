package io.github.raphaelmun1z.ecommerce.services.usuario;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Usuario;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(String id) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        if (usuario.getPapel() != null) {
            usuario.getPapel().getNome();
        }

        return usuario;
    }

    @Transactional
    public void atualizarFotoPerfil(String id, String fileName) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setAvatar(fileName);
        repository.save(usuario);
    }

}

