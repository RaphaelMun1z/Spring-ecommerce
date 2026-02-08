package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.PedidoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.carrinho.Carrinho;
import io.github.raphaelmun1z.ecommerce.entities.carrinho.ItemCarrinho;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.enums.MetodoPagamento;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPagamento;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoMovimentacao;
import io.github.raphaelmun1z.ecommerce.entities.estoque.MovimentacaoEstoque;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Entrega;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.ItemPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pagamento;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Endereco;
import io.github.raphaelmun1z.ecommerce.exceptions.models.NotFoundException;
import io.github.raphaelmun1z.ecommerce.payment.AbacatePayService;
import io.github.raphaelmun1z.ecommerce.payment.dto.AbacatePayResponse;
import io.github.raphaelmun1z.ecommerce.payment.dto.CreateBillingRequest;
import io.github.raphaelmun1z.ecommerce.payment.dto.Customer;
import io.github.raphaelmun1z.ecommerce.payment.dto.Product;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.CarrinhoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PedidoRepository;
import io.github.raphaelmun1z.ecommerce.services.catalogo.EstoqueService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoService carrinhoService;
    private final EstoqueService estoqueService;
    private final AbacatePayService abacatePayService;

    @Value("${app.url.sucesso:http://localhost:4200/pedido/sucesso}")
    private String urlSucesso;

    @Value("${app.url.falha:http://localhost:4200/pedido/falhou}")
    private String urlFalha;

    public PedidoService(PedidoRepository pedidoRepository,
                         CarrinhoRepository carrinhoRepository,
                         CarrinhoService carrinhoService,
                         EstoqueService estoqueService,
                         AbacatePayService abacatePayService) {
        this.pedidoRepository = pedidoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.carrinhoService = carrinhoService;
        this.estoqueService = estoqueService;
        this.abacatePayService = abacatePayService;
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
    public PedidoResponseDTO criarPedidoDoCarrinho(String clienteId, BigDecimal valorFrete, String enderecoId, MetodoPagamento metodoEscolhido) {
        // 1. Validações Iniciais
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new NotFoundException("Carrinho não encontrado para o cliente: " + clienteId));

        if (carrinho.getItens().isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio.");
        }

        Cliente cliente = carrinho.getCliente();

        // 2. Resolução do Endereço
        Endereco enderecoSelecionado = cliente.getEnderecos().stream()
                .filter(e -> e.getId().equals(enderecoId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Endereço de entrega inválido."));

        // 3. Montagem do Pedido
        Pedido pedido = new Pedido(cliente);
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

        Entrega entrega = montarObjetoEntrega(enderecoSelecionado, valorFrete);
        pedido.setEntrega(entrega);

        // 4. Processamento de Itens e Estoque
        processarItensEEstoque(pedido, carrinho, clienteId);

        pedido.calculaTotal();

        // 5. Integração com AbacatePay
        AbacatePayResponse dadosPagamentoExterno = gerarCobrancaAbacatePay(pedido, cliente, metodoEscolhido);

        // 6. Configuração do Pagamento Interno
        Pagamento pagamento = new Pagamento();
        pagamento.setGateway("ABACATE_PAY");
        pagamento.setMetodo(metodoEscolhido);
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setValor(pedido.getValorTotal());
        pagamento.setNumeroParcelas(1);
        pagamento.setPedido(pedido);
        pagamento.setBillingId(dadosPagamentoExterno.data().id());
        pagamento.setUrlPagamento(dadosPagamentoExterno.data().url());
        pedido.setPagamento(pagamento);

        // 7. Finalização
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        carrinhoService.limparCarrinho(clienteId);

        return new PedidoResponseDTO(pedidoSalvo);
    }

    @Transactional
    public AbacatePayResponse gerarLinkPagamento(String pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado. Id: " + pedidoId));

        // Validação básica de estado
        if (pedido.getStatus() == StatusPedido.CANCELADO || pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new IllegalStateException("Não é possível gerar pagamento para pedido finalizado.");
        }

        // Reutiliza a lógica privada de integração
        AbacatePayResponse response = gerarCobrancaAbacatePay(pedido, pedido.getCliente(), pedido.getPagamento().getMetodo());

        // Atualiza as informações de pagamento no pedido existente
        Pagamento pagamento = pedido.getPagamento();
        pagamento.setBillingId(response.data().id());
        pagamento.setUrlPagamento(response.data().url());

        pedidoRepository.save(pedido);

        return response;
    }

    private Entrega montarObjetoEntrega(Endereco endereco, BigDecimal valorFrete) {
        Entrega entrega = new Entrega();
        entrega.setValorFrete(valorFrete != null ? valorFrete : BigDecimal.ZERO);
        entrega.setCep(endereco.getCep());
        entrega.setLogradouro(endereco.getLogradouro());
        entrega.setNumero(endereco.getNumero());
        entrega.setComplemento(endereco.getComplemento());
        entrega.setBairro(endereco.getBairro());
        entrega.setCidade(endereco.getCidade());
        entrega.setUf(endereco.getUf());
        entrega.setPrazoDiasUteis(7);
        entrega.setTransportadora("Padrão");
        return entrega;
    }

    private void processarItensEEstoque(Pedido pedido, Carrinho carrinho, String clienteId) {
        for (ItemCarrinho itemCarrinho : carrinho.getItens()) {
            Produto produto = itemCarrinho.getProduto();

            if (produto.getEstoque() < itemCarrinho.getQuantidade()) {
                throw new IllegalStateException("Estoque insuficiente para: " + produto.getTitulo());
            }

            BigDecimal precoFinal = produto.getPrecoPromocional() != null
                    ? produto.getPrecoPromocional()
                    : produto.getPreco();

            ItemPedido itemPedido = new ItemPedido(pedido, produto, itemCarrinho.getQuantidade(), precoFinal);
            pedido.adicionarItem(itemPedido);

            MovimentacaoEstoque mov = new MovimentacaoEstoque(
                    produto,
                    itemCarrinho.getQuantidade(),
                    TipoMovimentacao.SAIDA,
                    "Venda Checkout - Pedido do Cliente: " + clienteId
            );
            estoqueService.registrarMovimentacao(mov);
        }
    }

    private AbacatePayResponse gerarCobrancaAbacatePay(Pedido pedido, Cliente cliente, MetodoPagamento metodo) {
        List<Product> produtosApi = new ArrayList<>();

        for (ItemPedido item : pedido.getItens()) {
            int precoCentavos = item.getPrecoUnitario().multiply(new BigDecimal("100")).intValue();

            produtosApi.add(new Product(
                    item.getProduto().getId(),
                    item.getProduto().getTitulo(),
                    item.getProduto().getDescricao() != null ? item.getProduto().getDescricao() : "Sem descrição",
                    item.getQuantidade(),
                    precoCentavos
            ));
        }

        BigDecimal valorFrete = pedido.getEntrega().getValorFrete();
        if (valorFrete != null && valorFrete.compareTo(BigDecimal.ZERO) > 0) {
            int freteCentavos = valorFrete.multiply(new BigDecimal("100")).intValue();
            produtosApi.add(new Product(
                    "FRETE-" + pedido.getId(),
                    "Taxa de Entrega / Frete",
                    "Serviço de entrega do pedido",
                    1,
                    freteCentavos
            ));
        }

        String cpfFormatado = formatarCpf(cliente.getCpf());
        String telefoneFormatado = formatarTelefone(cliente.getTelefone());

        Customer customerApi = new Customer(
                cliente.getNome(),
                telefoneFormatado,
                cliente.getEmail(),
                cpfFormatado
        );

        String metodoApi = switch (metodo) {
            case PIX -> "PIX";
            case CARTAO_CREDITO -> "CREDIT_CARD";
            case BOLETO -> "BOLETO";
            default -> "PIX";
        };

        CreateBillingRequest request = new CreateBillingRequest(
                "ONE_TIME",
                List.of(metodoApi),
                produtosApi,
                this.urlFalha,
                this.urlSucesso,
                customerApi
        );

        return abacatePayService.criarCobranca(request);
    }

    private String formatarCpf(String cpf) {
        if (cpf == null) return "";
        String limpo = cpf.replaceAll("\\D", "");
        if (limpo.length() != 11) return cpf;
        return limpo.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String formatarTelefone(String telefone) {
        if (telefone == null) return "";
        String limpo = telefone.replaceAll("\\D", "");
        if (limpo.length() == 11) {
            return limpo.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }
        if (limpo.length() == 10) {
            return limpo.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        return telefone;
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(String id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado. Id: " + id));

        StatusPedido statusAtual = pedido.getStatus();

        if (statusAtual == novoStatus) return new PedidoResponseDTO(pedido);

        pedido.setStatus(novoStatus);
        return new PedidoResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public void confirmarPagamento(String billingId, String transactionId) {
        Pedido pedido = pedidoRepository.findByPagamento_BillingId(billingId)
                .orElseThrow(() -> new NotFoundException(
                        "Pedido não encontrado para o ID de cobrança: " + billingId));

        Pagamento pagamento = pedido.getPagamento();

        if (pagamento.getStatus() == StatusPagamento.APROVADO) {
            return;
        }

        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamento.setCodigoTransacaoGateway(transactionId);
        pagamento.setDataConfirmacao(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PAGO);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void cancelarPagamento(String billingId) {
        Pedido pedido = pedidoRepository.findByPagamento_BillingId(billingId)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado: " + billingId));

        if (pedido.getStatus() == StatusPedido.CANCELADO) return;

        pedido.getPagamento().setStatus(StatusPagamento.CANCELADO);
        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }
}
