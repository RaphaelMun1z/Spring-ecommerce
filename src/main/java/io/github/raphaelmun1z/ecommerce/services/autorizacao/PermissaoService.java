package io.github.raphaelmun1z.ecommerce.services.autorizacao;

import io.github.raphaelmun1z.ecommerce.dtos.res.autorizacao.PermissaoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Permissao;
import io.github.raphaelmun1z.ecommerce.exceptions.models.NotFoundException;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PermissaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public PermissaoService(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    @Transactional(readOnly = true)
    public List<PermissaoResponseDTO> buscarTodas() {
        return permissaoRepository.findAll().stream()
                .map(PermissaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PermissaoResponseDTO buscarPorId(String id) {
        Permissao permissao = permissaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Permissão não encontrada com o ID: " + id));
        return new PermissaoResponseDTO(permissao);
    }
}