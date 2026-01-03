package io.github.raphaelmun1z.ecommerce.services.catalogo;

import io.github.raphaelmun1z.ecommerce.dtos.req.catalogo.MovimentacaoEstoqueRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.catalogo.MovimentacaoEstoqueResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoMovimentacao;
import io.github.raphaelmun1z.ecommerce.entities.estoque.MovimentacaoEstoque;
import io.github.raphaelmun1z.ecommerce.exceptions.models.NotFoundException;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.EstoqueRepository;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponseDTO> buscarHistoricoPorProduto(String produtoId, Pageable pageable) {
        if (!produtoRepository.existsById(produtoId)) {
            throw new NotFoundException("Produto não encontrado. Id: " + produtoId);
        }
        return estoqueRepository.findByProdutoId(produtoId, pageable)
            .map(MovimentacaoEstoqueResponseDTO::new);
    }

    @Transactional
    public MovimentacaoEstoqueResponseDTO registrarMovimentacao(MovimentacaoEstoque movimentacao) {
        Produto produto = produtoRepository.findById(movimentacao.getProduto().getId())
            .orElseThrow(() -> new NotFoundException("Produto não encontrado para movimentação."));

        atualizarSaldoProduto(produto, movimentacao.getQuantidade(), movimentacao.getTipo());

        produtoRepository.save(produto);

        movimentacao.setProduto(produto);
        MovimentacaoEstoque salvo = estoqueRepository.save(movimentacao);

        return new MovimentacaoEstoqueResponseDTO(salvo);
    }

    @Transactional
    public MovimentacaoEstoqueResponseDTO registrarMovimentacaoManual(MovimentacaoEstoqueRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
            .orElseThrow(() -> new NotFoundException("Produto não encontrado. Id: " + dto.getProdutoId()));

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(dto.getQuantidade());
        movimentacao.setTipo(dto.getTipo());
        movimentacao.setMotivo(dto.getMotivo());

        return registrarMovimentacao(movimentacao);
    }

    private void atualizarSaldoProduto(Produto produto, Integer quantidade, TipoMovimentacao tipo) {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade movimentada deve ser maior que zero.");
        }

        if (tipo == TipoMovimentacao.ENTRADA) {
            produto.setEstoque(produto.getEstoque() + quantidade);
        } else if (tipo == TipoMovimentacao.SAIDA) {
            if (produto.getEstoque() < quantidade) {
                throw new IllegalArgumentException("Estoque insuficiente para realizar a saída. Estoque atual: " + produto.getEstoque());
            }
            produto.setEstoque(produto.getEstoque() - quantidade);
        }
    }
}