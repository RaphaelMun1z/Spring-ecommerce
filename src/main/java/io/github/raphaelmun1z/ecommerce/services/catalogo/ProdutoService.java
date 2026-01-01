package io.github.raphaelmun1z.ecommerce.services.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Produto> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Produto> findAllActive(Pageable pageable) {
        return repository.findByAtivoTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Produto findById(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado. Id: " + id));
    }

    @Transactional
    public Produto insert(Produto obj) {
        if (repository.existsByCodigoControle(obj.getCodigoControle())) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com o código de controle (SKU): " + obj.getCodigoControle());
        }

        validarRegrasDeNegocio(obj);

        if (obj.getAtivo() == null) obj.setAtivo(true);

        return repository.save(obj);
    }

    @Transactional
    public Produto update(String id, Produto obj) {
        try {
            Produto entity = repository.getReferenceById(id);

            validarRegrasDeNegocio(obj);

            updateData(entity, obj);
            return repository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Produto não encontrado para atualização. Id: " + id);
        }
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Produto não encontrado para exclusão. Id: " + id);
        }
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Integridade crítica: Não é possível excluir este produto pois ele possui histórico de vendas ou movimentações. Sugestão: Utilize a opção de desativar o produto.");
        }
    }

    @Transactional
    public void desativar(String id) {
        Produto entity = findById(id);
        if (!entity.getAtivo()) {
            throw new IllegalArgumentException("O produto já está desativado.");
        }
        entity.setAtivo(false);
        repository.save(entity);
    }

    @Transactional
    public void ativar(String id) {
        Produto entity = findById(id);
        if (entity.getAtivo()) {
            throw new IllegalArgumentException("O produto já está ativo.");
        }
        entity.setAtivo(true);
        repository.save(entity);
    }

    private void updateData(Produto entity, Produto obj) {
        entity.setTitulo(obj.getTitulo());
        entity.setDescricao(obj.getDescricao());
        entity.setPreco(obj.getPreco());
        entity.setPrecoPromocional(obj.getPrecoPromocional());
        entity.setEstoque(obj.getEstoque());
        entity.setAtivo(obj.getAtivo());
        entity.setPesoKg(obj.getPesoKg());
        entity.setDimensoes(obj.getDimensoes());
    }

    private void validarRegrasDeNegocio(Produto p) {
        if (p.getPreco() == null || p.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }

        if (p.getPrecoPromocional() != null) {
            if (p.getPrecoPromocional().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("O preço promocional deve ser maior que zero.");
            }
            if (p.getPrecoPromocional().compareTo(p.getPreco()) >= 0) {
                throw new IllegalArgumentException("O preço promocional deve ser menor que o preço original.");
            }
        }

        if (p.getEstoque() == null || p.getEstoque() < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
    }
}
