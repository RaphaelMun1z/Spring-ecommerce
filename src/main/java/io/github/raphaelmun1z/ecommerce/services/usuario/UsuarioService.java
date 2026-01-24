package io.github.raphaelmun1z.ecommerce.services.usuario;

import io.github.raphaelmun1z.ecommerce.dtos.req.usuario.ClienteUpdateRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.usuario.UsuarioResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Usuario;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(UsuarioResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> buscarPorTermo(String termo, Pageable pageable) {
        if (termo == null || termo.trim().isEmpty()) {
            return repository.findAll(pageable)
                    .map(UsuarioResponseDTO::new);
        }

        return repository.buscarPorNomeOuEmail(termo, pageable)
                .map(UsuarioResponseDTO::new);
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

    @Transactional
    public void atualizarDadosCliente(String id, ClienteUpdateRequestDTO dto) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(dto.nome());

        if (usuario instanceof Cliente cliente) {
            cliente.setTelefone(dto.telefone());
        }

        repository.save(usuario);
    }
}

