package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPagamento;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pagamento;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PagamentoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PagamentoService {
    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, PedidoRepository pedidoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public Pagamento buscarPorId(String id) {
        return pagamentoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado. Id do Pedido: " + id));
    }

    @Transactional
    public Pagamento criarPagamento(Pagamento pagamento, String pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado. Id: " + pedidoId));

        if (pagamentoRepository.existsById(pedidoId)) {
            throw new IllegalStateException("Já existe uma tentativa de pagamento registrada para este pedido.");
        }

        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new IllegalStateException("Não é possível pagar um pedido que não está aguardando pagamento. Status atual: " + pedido.getStatus());
        }

        if (pagamento.getValor().compareTo(pedido.getValorTotal()) != 0) {
            throw new IllegalArgumentException("O valor do pagamento (" + pagamento.getValor() +
                ") diverge do total do pedido (" + pedido.getValorTotal() + ").");
        }

        pagamento.setPedido(pedido);
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setDataPagamento(LocalDateTime.now());

        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public Pagamento atualizarStatus(String id, StatusPagamento novoStatus, String codigoGateway) {
        Pagamento pagamento = buscarPorId(id);
        StatusPagamento statusAtual = pagamento.getStatus();

        if (statusAtual == novoStatus) {
            return pagamento;
        }

        validarTransicaoStatus(statusAtual, novoStatus);

        pagamento.setStatus(novoStatus);
        if (codigoGateway != null && !codigoGateway.isBlank()) {
            pagamento.setCodigoTransacaoGateway(codigoGateway);
        }

        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        sincronizarStatusPedido(pagamentoSalvo.getPedido(), novoStatus);

        return pagamentoSalvo;
    }

    private void validarTransicaoStatus(StatusPagamento atual, StatusPagamento novo) {
        if (atual == StatusPagamento.APROVADO || atual == StatusPagamento.ESTORNADO) {
            if (atual == StatusPagamento.APROVADO && novo == StatusPagamento.ESTORNADO) {
                return;
            }
            throw new IllegalStateException("Não é possível alterar um pagamento finalizado (" + atual + ").");
        }

        if ((atual == StatusPagamento.CANCELADO || atual == StatusPagamento.RECUSADO) && novo == StatusPagamento.APROVADO) {
            throw new IllegalStateException("Pagamentos recusados ou cancelados não podem ser aprovados posteriormente. Gere uma nova transação.");
        }
    }

    private void sincronizarStatusPedido(Pedido pedido, StatusPagamento statusPagamento) {
        if (pedido == null) return;

        if (statusPagamento == StatusPagamento.APROVADO) {
            if (pedido.getStatus() == StatusPedido.AGUARDANDO_PAGAMENTO) {
                pedido.setStatus(StatusPedido.PAGO);
            }
        } else if (statusPagamento == StatusPagamento.ESTORNADO) {
            pedido.setStatus(StatusPedido.CANCELADO);
        }

        pedidoRepository.save(pedido);
    }
}
