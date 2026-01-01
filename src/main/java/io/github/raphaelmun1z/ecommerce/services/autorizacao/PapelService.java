package io.github.raphaelmun1z.ecommerce.services.autorizacao;


import io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao.PapelRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.autorizacao.PapelResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Permissao;
import io.github.raphaelmun1z.ecommerce.exceptions.models.BusinessException;
import io.github.raphaelmun1z.ecommerce.exceptions.models.NotFoundException;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PapelRepository;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PermissaoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PapelService {
    private final PapelRepository papelRepository;
    private final PermissaoRepository permissaoRepository;
    private final UsuarioRepository usuarioRepository;

    public PapelService(
            PapelRepository papelRepository,
            PermissaoRepository permissaoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.papelRepository = papelRepository;
        this.permissaoRepository = permissaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public PapelResponseDTO criar(PapelRequestDTO dto) {
        if (papelRepository.findByNome(dto.nome()).isPresent()) {
            throw new BusinessException("Já existe um papel com o nome: " + dto.nome());
        }

        Set<Permissao> permissoes = buscarPermissoes(dto.permissoesIds());

        Papel novoPapel = new Papel();
        novoPapel.setNome(dto.nome().toUpperCase());
        novoPapel.setPermissoes(permissoes);

        Papel papelSalvo = papelRepository.save(novoPapel);
        return PapelResponseDTO.fromEntity(papelSalvo);
    }

    @Transactional(readOnly = true)
    public List<PapelResponseDTO> buscarTodos() {
        return papelRepository.findAll().stream().map(PapelResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PapelResponseDTO buscarPorId(String id) {
        Papel papel = papelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Papel não encontrado com o ID: " + id));
        return PapelResponseDTO.fromEntity(papel);
    }

    @Transactional
    public PapelResponseDTO atualizar(String id, PapelRequestDTO dto) {
        Papel papelExistente = papelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Papel não encontrado com o ID: " + id));

        papelRepository.findByNome(dto.nome()).ifPresent(papelComMesmoNome -> {
            if (!papelComMesmoNome.getId().equals(id)) {
                throw new BusinessException("Já existe outro papel com o nome: " + dto.nome());
            }
        });

        Set<Permissao> permissoes = buscarPermissoes(dto.permissoesIds());

        papelExistente.setNome(dto.nome().toUpperCase());
        papelExistente.setPermissoes(permissoes);

        Papel papelAtualizado = papelRepository.save(papelExistente);
        return PapelResponseDTO.fromEntity(papelAtualizado);
    }

    @Transactional
    public void deletar(String id) {
        if (!papelRepository.existsById(id)) {
            throw new NotFoundException("Papel não encontrado com o ID: " + id);
        }

        if (usuarioRepository.existsByPapel_Id(id)) {
            throw new BusinessException("Não é possível excluir um papel que está atribuído a um ou mais usuários.");
        }

        papelRepository.deleteById(id);
    }

    private Set<Permissao> buscarPermissoes(Set<String> ids) {
        Set<Permissao> permissoes = new HashSet<>(permissaoRepository.findAllById(ids));
        if (permissoes.size() != ids.size()) {
            throw new NotFoundException("Uma ou mais permissões não foram encontradas.");
        }
        return permissoes;
    }
}
