package io.github.raphaelmun1z.ecommerce.services.catalogo;

import io.github.raphaelmun1z.ecommerce.dtos.req.catalogo.CategoriaRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.catalogo.CategoriaResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Categoria;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(CategoriaResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> findAllActive(Pageable pageable) {
        return repository.findByAtivaTrue(pageable).map(CategoriaResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO findById(String id) {
        Categoria entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada. Id: " + id));
        return new CategoriaResponseDTO(entity);
    }

    @Transactional
    public CategoriaResponseDTO insert(CategoriaRequestDTO dto) {
        validarUnicidade(dto.getNome(), dto.getSlug());

        Categoria entity = new Categoria();
        converterDtoParaEntidade(dto, entity);

        Categoria savedObj = repository.save(entity);
        return new CategoriaResponseDTO(savedObj);
    }

    @Transactional
    public CategoriaResponseDTO update(String id, CategoriaRequestDTO dto) {
        try {
            Categoria entity = repository.getReferenceById(id);

            // Validações simples para garantir que não está duplicando dados únicos
            // Nota: Em produção, ideal validar se o nome/slug pertence a OUTRO id

            updateData(entity, dto);
            Categoria savedObj = repository.save(entity);
            return new CategoriaResponseDTO(savedObj);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Categoria não encontrada para atualização. Id: " + id);
        }
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Categoria não encontrada para exclusão. Id: " + id);
        }
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível excluir esta categoria pois ela possui produtos ou subcategorias associadas.");
        }
    }

    @Transactional
    public void desativar(String id) {
        Categoria entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada. Id: " + id));
        entity.setAtiva(false);
        repository.save(entity);
    }

    @Transactional
    public void ativar(String id) {
        Categoria entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada. Id: " + id));
        entity.setAtiva(true);
        repository.save(entity);
    }

    private void converterDtoParaEntidade(CategoriaRequestDTO dto, Categoria entity) {
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setSlug(dto.getSlug());
        entity.setAtiva(dto.getAtiva() != null ? dto.getAtiva() : true);

        if (dto.getCategoriaPaiId() != null) {
            Categoria pai = repository.findById(dto.getCategoriaPaiId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria Pai não encontrada. Id: " + dto.getCategoriaPaiId()));
            entity.setCategoriaPai(pai);
        } else {
            entity.setCategoriaPai(null);
        }
    }

    private void updateData(Categoria entity, CategoriaRequestDTO dto) {
        entity.setNome(dto.getNome());
        entity.setSlug(dto.getSlug());
        entity.setDescricao(dto.getDescricao());
    }

    private void validarUnicidade(String nome, String slug) {
        if (repository.existsByNome(nome)) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome.");
        }
        if (repository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Já existe uma categoria com este slug.");
        }
    }
}