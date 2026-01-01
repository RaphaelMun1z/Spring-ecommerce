package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.carrinho.Carrinho;
import io.github.raphaelmun1z.ecommerce.entities.carrinho.ItemCarrinho;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoMovimentacao;
import io.github.raphaelmun1z.ecommerce.entities.estoque.MovimentacaoEstoque;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.ItemPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PedidoRepository;
import io.github.raphaelmun1z.ecommerce.services.catalogo.EstoqueService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final CarrinhoService carrinhoService;
    private final EstoqueService estoqueService;

    public PedidoService(PedidoRepository pedidoRepository,
                         CarrinhoService carrinhoService,
                         EstoqueService estoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.carrinhoService = carrinhoService;
        this.estoqueService = estoqueService;
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(String id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado. Id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Pedido> listarPorCliente(String clienteId, Pageable pageable) {
        return pedidoRepository.findByClienteId(clienteId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Pedido> listarTodos(Pageable pageable) {
        return pedidoRepository.findAll(pageable);
    }

    @Transactional
    public Pedido criarPedidoDoCarrinho(String clienteId) {
        Carrinho carrinho = carrinhoService.buscarOuCriarCarrinho(clienteId);

        if (carrinho.getItens().isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio. Não é possível criar um pedido.");
        }

        Pedido pedido = new Pedido(carrinho.getCliente());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

        for (ItemCarrinho itemCarrinho : carrinho.getItens()) {
            Produto produto = itemCarrinho.getProduto();

            if (produto.getEstoque() < itemCarrinho.getQuantidade()) {
                throw new IllegalStateException("Estoque insuficiente para o produto: " + produto.getTitulo());
            }

            BigDecimal precoSnapshot = produto.getPrecoPromocional() != null
                ? produto.getPrecoPromocional()
                : produto.getPreco();

            ItemPedido itemPedido = new ItemPedido(pedido, produto, itemCarrinho.getQuantidade(), precoSnapshot);
            pedido.adicionarItem(itemPedido);

            MovimentacaoEstoque mov = new MovimentacaoEstoque(
                produto,
                itemCarrinho.getQuantidade(),
                TipoMovimentacao.SAIDA,
                "Venda Checkout - Cliente: " + clienteId
            );
            estoqueService.registrarMovimentacao(mov);
        }

        pedido.calculaTotal();

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        carrinhoService.limparCarrinho(clienteId);

        return pedidoSalvo;
    }

    @Transactional
    public Pedido atualizarStatus(String id, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);
        StatusPedido statusAtual = pedido.getStatus();

        if (statusAtual == novoStatus) {
            return pedido; // Nenhuma alteração necessária
        }

        if (statusAtual == StatusPedido.CANCELADO || statusAtual == StatusPedido.ENTREGUE) {
            throw new IllegalStateException("Não é possível alterar o status de um pedido já finalizado (" + statusAtual + ").");
        }

        boolean transicaoValida = switch (statusAtual) {
            case AGUARDANDO_PAGAMENTO -> novoStatus == StatusPedido.PAGO || novoStatus == StatusPedido.CANCELADO;
            case PAGO ->
                novoStatus == StatusPedido.EM_PREPARACAO || novoStatus == StatusPedido.ENVIADO || novoStatus == StatusPedido.CANCELADO;
            case EM_PREPARACAO -> novoStatus == StatusPedido.ENVIADO || novoStatus == StatusPedido.CANCELADO;
            case ENVIADO -> novoStatus == StatusPedido.ENTREGUE;
            default -> false;
        };

        if (!transicaoValida) {
            throw new IllegalStateException(
                String.format("Transição de status inválida: Não é permitido mudar de '%s' para '%s'.", statusAtual, novoStatus)
            );
        }

        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }
}
