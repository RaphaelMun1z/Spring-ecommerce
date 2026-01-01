package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEntrega;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Entrega;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.EntregaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EntregaService {
    private final EntregaRepository entregaRepository;
    private final PedidoRepository pedidoRepository;

    public EntregaService(EntregaRepository entregaRepository, PedidoRepository pedidoRepository) {
        this.entregaRepository = entregaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public Entrega buscarPorId(String id) {
        return entregaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada. Id: " + id));
    }

    @Transactional(readOnly = true)
    public Entrega buscarPorPedido(String pedidoId) {
        return entregaRepository.findByPedidoId(pedidoId)
            .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada para o pedido: " + pedidoId));
    }

    @Transactional
    public Entrega criarEntrega(Entrega entrega, String pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado. Id: " + pedidoId));

        if (pedido.getEntrega() != null) {
            throw new IllegalStateException("Este pedido já possui uma entrega associada.");
        }

        entrega.setPedido(pedido);
        entrega.setStatus(StatusEntrega.PENDENTE);

        if (entrega.getDataEstimadaEntrega() == null && entrega.getPrazoDiasUteis() != null) {
            entrega.setDataEstimadaEntrega(LocalDateTime.now().plusDays(entrega.getPrazoDiasUteis()));
        }

        return entregaRepository.save(entrega);
    }

    @Transactional
    public Entrega atualizarRastreio(String entregaId, String codigoRastreio, String transportadora) {
        Entrega entrega = buscarPorId(entregaId);

        if (entrega.getStatus() == StatusEntrega.ENTREGUE || entrega.getStatus() == StatusEntrega.CANCELADO) {
            throw new IllegalStateException("Não é possível alterar rastreio de uma entrega finalizada.");
        }

        entrega.setCodigoRastreio(codigoRastreio);
        if (transportadora != null && !transportadora.isBlank()) {
            entrega.setTransportadora(transportadora);
        }

        if (entrega.getStatus() == StatusEntrega.PENDENTE || entrega.getStatus() == StatusEntrega.EM_SEPARACAO) {
            entrega.setStatus(StatusEntrega.ENVIADO);
            entrega.setDataEnvio(LocalDateTime.now());
        }

        return entregaRepository.save(entrega);
    }

    @Transactional
    public Entrega confirmarEntrega(String entregaId) {
        Entrega entrega = buscarPorId(entregaId);

        if (entrega.getStatus() == StatusEntrega.CANCELADO) {
            throw new IllegalStateException("Entrega está cancelada.");
        }

        entrega.setStatus(StatusEntrega.ENTREGUE);
        entrega.setDataEntregaReal(LocalDateTime.now());

        Pedido pedido = entrega.getPedido();
        if (pedido != null) {
            pedido.setStatus(StatusPedido.ENTREGUE);
            pedidoRepository.save(pedido);
        }

        return entregaRepository.save(entrega);
    }

    @Transactional
    public Entrega atualizarStatus(String entregaId, StatusEntrega novoStatus) {
        Entrega entrega = buscarPorId(entregaId);
        entrega.setStatus(novoStatus);
        return entregaRepository.save(entrega);
    }
}
