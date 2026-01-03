package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.PedidoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.carrinho.Carrinho;
import io.github.raphaelmun1z.ecommerce.entities.carrinho.ItemCarrinho;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoMovimentacao;
import io.github.raphaelmun1z.ecommerce.entities.estoque.MovimentacaoEstoque;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.ItemPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.exceptions.models.NotFoundException;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.CarrinhoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PedidoRepository;
import io.github.raphaelmun1z.ecommerce.services.catalogo.EstoqueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoService carrinhoService;
    private final EstoqueService estoqueService;

    public PedidoService(PedidoRepository pedidoRepository,
                         CarrinhoRepository carrinhoRepository,
                         CarrinhoService carrinhoService,
                         EstoqueService estoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.carrinhoService = carrinhoService;
        this.estoqueService = estoqueService;
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(String id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Pedido não encontrado. Id: " + id));
        return new PedidoResponseDTO(pedido);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listarPorCliente(String clienteId, Pageable pageable) {
        return pedidoRepository.findByClienteId(clienteId, pageable)
            .map(PedidoResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listarTodos(Pageable pageable) {
        return pedidoRepository.findAll(pageable)
            .map(PedidoResponseDTO::new);
    }

    @Transactional
    public PedidoResponseDTO criarPedidoDoCarrinho(String clienteId) {
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
            .orElseThrow(() -> new NotFoundException("Carrinho não encontrado para o cliente: " + clienteId));

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

        return new PedidoResponseDTO(pedidoSalvo);
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(String id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Pedido não encontrado. Id: " + id));

        StatusPedido statusAtual = pedido.getStatus();

        if (statusAtual == novoStatus) {
            return new PedidoResponseDTO(pedido);
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
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(pedidoSalvo);
    }
}
