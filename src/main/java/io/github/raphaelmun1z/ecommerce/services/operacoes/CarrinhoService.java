package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.carrinho.Carrinho;
import io.github.raphaelmun1z.ecommerce.entities.carrinho.ItemCarrinho;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.CarrinhoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CarrinhoService {
    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository,
                           ProdutoRepository produtoRepository,
                           ClienteRepository clienteRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Carrinho buscarOuCriarCarrinho(String clienteId) {
        return carrinhoRepository.findByClienteId(clienteId)
            .orElseGet(() -> criarNovoCarrinho(clienteId));
    }

    @Transactional
    public Carrinho adicionarItem(String clienteId, String produtoId, Integer quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        Carrinho carrinho = buscarOuCriarCarrinho(clienteId);
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado. Id: " + produtoId));

        if (!produto.getAtivo()) {
            throw new IllegalArgumentException("Produto indisponível ou inativo.");
        }

        Optional<ItemCarrinho> itemExistente = carrinho.getItens().stream()
            .filter(item -> item.getProduto().getId().equals(produtoId))
            .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrinho item = itemExistente.get();
            int novaQuantidade = item.getQuantidade() + quantidade;
            validarEstoque(produto, novaQuantidade);
            item.setQuantidade(novaQuantidade);
        } else {
            validarEstoque(produto, quantidade);
            ItemCarrinho novoItem = new ItemCarrinho(carrinho, produto, quantidade);
            carrinho.adicionarItem(novoItem);
        }

        return carrinhoRepository.save(carrinho);
    }

    @Transactional
    public Carrinho removerItem(String clienteId, String produtoId) {
        Carrinho carrinho = buscarOuCriarCarrinho(clienteId);

        ItemCarrinho itemParaRemover = carrinho.getItens().stream()
            .filter(item -> item.getProduto().getId().equals(produtoId))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Item não encontrado no carrinho."));

        carrinho.removerItem(itemParaRemover);
        return carrinhoRepository.save(carrinho);
    }

    @Transactional
    public Carrinho atualizarQuantidade(String clienteId, String produtoId, Integer novaQuantidade) {
        if (novaQuantidade <= 0) {
            return removerItem(clienteId, produtoId);
        }

        Carrinho carrinho = buscarOuCriarCarrinho(clienteId);
        ItemCarrinho item = carrinho.getItens().stream()
            .filter(i -> i.getProduto().getId().equals(produtoId))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Item não encontrado no carrinho."));

        validarEstoque(item.getProduto(), novaQuantidade);
        item.setQuantidade(novaQuantidade);

        return carrinhoRepository.save(carrinho);
    }

    @Transactional
    public void limparCarrinho(String clienteId) {
        Carrinho carrinho = buscarOuCriarCarrinho(clienteId);
        carrinho.getItens().clear(); // OrphanRemoval cuidará de deletar os itens do banco
        carrinhoRepository.save(carrinho);
    }

    private Carrinho criarNovoCarrinho(String clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado. Id: " + clienteId));

        Carrinho novoCarrinho = new Carrinho(cliente);
        return carrinhoRepository.save(novoCarrinho);
    }

    private void validarEstoque(Produto produto, Integer quantidadeDesejada) {
        if (produto.getEstoque() < quantidadeDesejada) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getTitulo() +
                ". Disponível: " + produto.getEstoque());
        }
    }
}
