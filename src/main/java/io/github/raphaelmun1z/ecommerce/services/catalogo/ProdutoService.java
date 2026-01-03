package io.github.raphaelmun1z.ecommerce.services.catalogo;

import io.github.raphaelmun1z.ecommerce.dtos.req.catalogo.ProdutoRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.catalogo.ProdutoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Categoria;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.exceptions.models.NotFoundException;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.CategoriaRepository;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(ProdutoResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> listarAtivos(Pageable pageable) {
        return repository.findByAtivoTrue(pageable).map(ProdutoResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(String id) {
        Produto produto = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Produto não encontrado. Id: " + id));
        return new ProdutoResponseDTO(produto);
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        if (repository.existsByCodigoControle(dto.getCodigoControle())) {
            throw new IllegalArgumentException("Já existe um produto com este SKU: " + dto.getCodigoControle());
        }

        Produto entity = new Produto();
        converterDtoParaEntidade(dto, entity);
        validarRegrasDeNegocio(entity);

        Produto saved = repository.save(entity);
        return new ProdutoResponseDTO(saved);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(String id, ProdutoRequestDTO dto) {
        Produto entity = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Produto não encontrado. Id: " + id));

        converterDtoParaEntidade(dto, entity);
        validarRegrasDeNegocio(entity);

        Produto saved = repository.save(entity);
        return new ProdutoResponseDTO(saved);
    }

    @Transactional
    public void excluir(String id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Produto não encontrado. Id: " + id);
        }
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível excluir este produto pois ele possui histórico de vendas.");
        }
    }

    @Transactional
    public void desativar(String id) {
        Produto entity = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Produto não encontrado. Id: " + id));

        if (!entity.getAtivo()) throw new IllegalArgumentException("O produto já está desativado.");

        entity.setAtivo(false);
        repository.save(entity);
    }

    @Transactional
    public void ativar(String id) {
        Produto entity = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Produto não encontrado. Id: " + id));

        if (entity.getAtivo()) throw new IllegalArgumentException("O produto já está ativo.");

        entity.setAtivo(true);
        repository.save(entity);
    }

    private void converterDtoParaEntidade(ProdutoRequestDTO dto, Produto entity) {
        entity.setCodigoControle(dto.getCodigoControle());
        entity.setTitulo(dto.getTitulo());
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getPreco());
        entity.setPrecoPromocional(dto.getPrecoPromocional());
        entity.setEstoque(dto.getEstoque());
        entity.setAtivo(dto.getAtivo());
        entity.setPesoKg(dto.getPesoKg());
        entity.setDimensoes(dto.getDimensoes());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada. Id: " + dto.getCategoriaId()));
            entity.setCategoria(categoria);
        } else {
            entity.setCategoria(null);
        }
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