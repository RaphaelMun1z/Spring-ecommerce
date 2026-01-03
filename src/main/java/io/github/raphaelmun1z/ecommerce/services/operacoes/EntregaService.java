package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.EntregaResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEntrega;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Entrega;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.EntregaRepository;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PedidoRepository;
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
    public EntregaResponseDTO buscarPorId(String id) {
        Entrega entrega = buscarEntidadePorId(id);
        return new EntregaResponseDTO(entrega);
    }

    @Transactional(readOnly = true)
    public EntregaResponseDTO buscarPorPedido(String pedidoId) {
        Entrega entrega = entregaRepository.findByPedidoId(pedidoId)
            .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada para o pedido: " + pedidoId));
        return new EntregaResponseDTO(entrega);
    }

    @Transactional
    public EntregaResponseDTO criarEntrega(Entrega entrega, String pedidoId) {
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

        Entrega entregaSalva = entregaRepository.save(entrega);
        return new EntregaResponseDTO(entregaSalva);
    }

    @Transactional
    public EntregaResponseDTO atualizarRastreio(String entregaId, String codigoRastreio, String transportadora) {
        Entrega entrega = buscarEntidadePorId(entregaId);

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

        Entrega entregaSalva = entregaRepository.save(entrega);
        return new EntregaResponseDTO(entregaSalva);
    }

    @Transactional
    public EntregaResponseDTO confirmarEntrega(String entregaId) {
        Entrega entrega = buscarEntidadePorId(entregaId);

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

        Entrega entregaSalva = entregaRepository.save(entrega);
        return new EntregaResponseDTO(entregaSalva);
    }

    @Transactional
    public EntregaResponseDTO atualizarStatus(String entregaId, StatusEntrega novoStatus) {
        Entrega entrega = buscarEntidadePorId(entregaId);
        entrega.setStatus(novoStatus);

        Entrega entregaSalva = entregaRepository.save(entrega);
        return new EntregaResponseDTO(entregaSalva);
    }

    private Entrega buscarEntidadePorId(String id) {
        return entregaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada. Id: " + id));
    }
}