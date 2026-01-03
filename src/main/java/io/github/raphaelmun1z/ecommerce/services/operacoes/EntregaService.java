package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.dtos.req.operacoes.EntregaRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.EntregaResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEntrega;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Entrega;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.exceptions.models.NotFoundException;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.EntregaRepository;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PedidoRepository;
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
            .orElseThrow(() -> new NotFoundException("Entrega não encontrada para o pedido: " + pedidoId));
        return new EntregaResponseDTO(entrega);
    }

    @Transactional
    public EntregaResponseDTO criarEntrega(EntregaRequestDTO dto, String pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new NotFoundException("Pedido não encontrado. Id: " + pedidoId));

        if (pedido.getEntrega() != null) {
            throw new IllegalStateException("Este pedido já possui uma entrega associada.");
        }

        Entrega entrega = new Entrega();
        converterDtoParaEntidade(dto, entrega);

        entrega.setPedido(pedido);
        entrega.setStatus(StatusEntrega.PENDENTE);

        // Define estimativa básica se não informada
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

        // Se o pedido estava pendente, assume que foi enviado ao adicionar rastreio
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

        // Atualiza status do pedido pai
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
            .orElseThrow(() -> new NotFoundException("Entrega não encontrada. Id: " + id));
    }

    private void converterDtoParaEntidade(EntregaRequestDTO dto, Entrega entity) {
        entity.setCodigoRastreio(dto.getCodigoRastreio());
        entity.setTransportadora(dto.getTransportadora());
        entity.setValorFrete(dto.getValorFrete());
        entity.setPrazoDiasUteis(dto.getPrazoDiasUteis());
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setNumero(dto.getNumero());
        entity.setComplemento(dto.getComplemento());
        entity.setBairro(dto.getBairro());
        entity.setCidade(dto.getCidade());
        entity.setUf(dto.getUf());
    }
}