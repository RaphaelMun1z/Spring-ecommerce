package io.github.raphaelmun1z.ecommerce.services.catalogo;

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
    public Page<Categoria> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Categoria> findAllActive(Pageable pageable) {
        return repository.findByAtivaTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Categoria findById(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada. Id: " + id));
    }

    @Transactional
    public Categoria insert(Categoria obj) {
        validarUnicidade(obj);
        return repository.save(obj);
    }

    @Transactional
    public Categoria update(String id, Categoria obj) {
        try {
            Categoria entity = repository.getReferenceById(id);
            updateData(entity, obj);
            return repository.save(entity);
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
        Categoria entity = findById(id);
        entity.setAtiva(false);
        repository.save(entity);
    }

    @Transactional
    public void ativar(String id) {
        Categoria entity = findById(id);
        entity.setAtiva(true);
        repository.save(entity);
    }

    private void updateData(Categoria entity, Categoria obj) {
        entity.setNome(obj.getNome());
        entity.setSlug(obj.getSlug());
        entity.setDescricao(obj.getDescricao());
        entity.setAtiva(obj.getAtiva());
    }

    private void validarUnicidade(Categoria obj) {
        if (repository.existsByNome(obj.getNome())) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome.");
        }
        if (repository.existsBySlug(obj.getSlug())) {
            throw new IllegalArgumentException("Já existe uma categoria com este slug.");
        }
    }
}
