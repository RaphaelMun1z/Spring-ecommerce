package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.dtos.req.operacoes.PagamentoRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.PagamentoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPagamento;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pagamento;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.exceptions.models.NotFoundException;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PagamentoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PagamentoService {
    private static final Logger log = LoggerFactory.getLogger(PagamentoService.class);

    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, PedidoRepository pedidoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public PagamentoResponseDTO buscarPorId(String id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado. Id: " + id));
        return new PagamentoResponseDTO(pagamento);
    }

    @Transactional
    public void processarConfirmacaoPagamento(String billingId) {
        Pagamento pagamento = pagamentoRepository.findByBillingId(billingId)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado para o billingId: " + billingId));

        if (pagamento.getStatus() == StatusPagamento.APROVADO) {
            log.info("Webhook ignorado: Pagamento já aprovado. BillingId: {}", billingId);
            return;
        }

        log.info("Processando aprovação de pagamento. BillingId: {}", billingId);

        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamento.setDataConfirmacao(LocalDateTime.now());

        atualizarStatusPedido(pagamento.getPedido(), StatusPedido.PAGO);

        pagamentoRepository.save(pagamento);
    }

    @Transactional
    public void processarCancelamentoPagamento(String billingId) {
        Pagamento pagamento = pagamentoRepository.findByBillingId(billingId)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado para o billingId: " + billingId));

        if (pagamento.getStatus() == StatusPagamento.CANCELADO || pagamento.getStatus() == StatusPagamento.RECUSADO) {
            return;
        }

        log.info("Processando cancelamento de pagamento. BillingId: {}", billingId);

        pagamento.setStatus(StatusPagamento.CANCELADO);

        atualizarStatusPedido(pagamento.getPedido(), StatusPedido.CANCELADO);

        pagamentoRepository.save(pagamento);
    }

    private void atualizarStatusPedido(Pedido pedido, StatusPedido novoStatus) {
        if (pedido == null) return;

        if (pedido.getStatus() == StatusPedido.ENVIADO || pedido.getStatus() == StatusPedido.ENTREGUE) {
            log.warn("Tentativa de alterar status de pedido já enviado. PedidoId: {}", pedido.getId());
            return;
        }

        pedido.setStatus(novoStatus);
        pedidoRepository.save(pedido);
    }
}