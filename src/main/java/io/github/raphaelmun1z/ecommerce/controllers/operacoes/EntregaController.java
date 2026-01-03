package io.github.raphaelmun1z.ecommerce.controllers.operacoes;

import io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs.EntregaControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.req.operacoes.EntregaRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.EntregaResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEntrega;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Entrega;
import io.github.raphaelmun1z.ecommerce.services.operacoes.EntregaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/entregas")
public class EntregaController implements EntregaControllerDocs {

    private final EntregaService service;

    public EntregaController(EntregaService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EntregaResponseDTO> buscarPorId(@PathVariable String id) {
        EntregaResponseDTO obj = service.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @Override
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<EntregaResponseDTO> buscarPorPedido(@PathVariable String pedidoId) {
        EntregaResponseDTO obj = service.buscarPorPedido(pedidoId);
        return ResponseEntity.ok(obj);
    }

    @Override
    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<EntregaResponseDTO> criarEntrega(@PathVariable String pedidoId, @Valid @RequestBody EntregaRequestDTO dto) {
        EntregaResponseDTO newObj = service.criarEntrega(dto, pedidoId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(newObj);
    }

    @Override
    @PatchMapping("/{id}/rastreio")
    public ResponseEntity<EntregaResponseDTO> atualizarRastreio(
        @PathVariable String id,
        @RequestParam String codigo,
        @RequestParam(required = false) String transportadora) {

        EntregaResponseDTO obj = service.atualizarRastreio(id, codigo, transportadora);
        return ResponseEntity.ok(obj);
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<EntregaResponseDTO> atualizarStatus(@PathVariable String id, @RequestParam StatusEntrega status) {
        EntregaResponseDTO obj = service.atualizarStatus(id, status);
        return ResponseEntity.ok(obj);
    }

    @Override
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<EntregaResponseDTO> confirmarEntrega(@PathVariable String id) {
        EntregaResponseDTO obj = service.confirmarEntrega(id);
        return ResponseEntity.ok(obj);
    }
}