package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.CarrinhoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.carrinho.Carrinho;
import io.github.raphaelmun1z.ecommerce.entities.carrinho.ItemCarrinho;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.exceptions.models.NotFoundException;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.CarrinhoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.ClienteRepository;
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
    public CarrinhoResponseDTO buscarCarrinho(String clienteId) {
        Carrinho carrinho = buscarOuCriarEntidade(clienteId);
        return new CarrinhoResponseDTO(carrinho);
    }

    @Transactional
    public CarrinhoResponseDTO adicionarItem(String clienteId, String produtoId, Integer quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        Carrinho carrinho = buscarOuCriarEntidade(clienteId);
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new NotFoundException("Produto não encontrado. Id: " + produtoId));

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

        Carrinho carrinhoSalvo = carrinhoRepository.save(carrinho);
        return new CarrinhoResponseDTO(carrinhoSalvo);
    }

    @Transactional
    public CarrinhoResponseDTO removerItem(String clienteId, String produtoId) {
        Carrinho carrinho = buscarOuCriarEntidade(clienteId);

        ItemCarrinho itemParaRemover = carrinho.getItens().stream()
            .filter(item -> item.getProduto().getId().equals(produtoId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Item não encontrado no carrinho."));

        carrinho.removerItem(itemParaRemover);

        Carrinho carrinhoSalvo = carrinhoRepository.save(carrinho);
        return new CarrinhoResponseDTO(carrinhoSalvo);
    }

    @Transactional
    public CarrinhoResponseDTO atualizarQuantidade(String clienteId, String produtoId, Integer novaQuantidade) {
        if (novaQuantidade <= 0) {
            return removerItem(clienteId, produtoId);
        }

        Carrinho carrinho = buscarOuCriarEntidade(clienteId);
        ItemCarrinho item = carrinho.getItens().stream()
            .filter(i -> i.getProduto().getId().equals(produtoId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Item não encontrado no carrinho."));

        validarEstoque(item.getProduto(), novaQuantidade);
        item.setQuantidade(novaQuantidade);

        Carrinho carrinhoSalvo = carrinhoRepository.save(carrinho);
        return new CarrinhoResponseDTO(carrinhoSalvo);
    }

    @Transactional
    public void limparCarrinho(String clienteId) {
        Carrinho carrinho = buscarOuCriarEntidade(clienteId);
        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);
    }

    private Carrinho buscarOuCriarEntidade(String clienteId) {
        return carrinhoRepository.findByClienteId(clienteId)
            .orElseGet(() -> criarNovoCarrinho(clienteId));
    }

    private Carrinho criarNovoCarrinho(String clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new NotFoundException("Cliente não encontrado. Id: " + clienteId));

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
