package io.github.raphaelmun1z.ecommerce.controllers.operacoes;

import io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs.EntregaControllerDocs;
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

    @GetMapping("/{id}")
    public ResponseEntity<Entrega> findById(@PathVariable String id) {
        Entrega obj = service.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Entrega> findByPedido(@PathVariable String pedidoId) {
        Entrega obj = service.buscarPorPedido(pedidoId);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<Entrega> create(@PathVariable String pedidoId, @Valid @RequestBody Entrega entrega) {
        Entrega newObj = service.criarEntrega(entrega, pedidoId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(newObj);
    }

    @PatchMapping("/{id}/rastreio")
    public ResponseEntity<Entrega> updateTracking(
        @PathVariable String id,
        @RequestParam String codigo,
        @RequestParam(required = false) String transportadora) {

        Entrega obj = service.atualizarRastreio(id, codigo, transportadora);
        return ResponseEntity.ok(obj);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Entrega> updateStatus(@PathVariable String id, @RequestParam StatusEntrega status) {
        Entrega obj = service.atualizarStatus(id, status);
        return ResponseEntity.ok(obj);
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Entrega> confirmDelivery(@PathVariable String id) {
        Entrega obj = service.confirmarEntrega(id);
        return ResponseEntity.ok(obj);
    }
}