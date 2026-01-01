package io.github.raphaelmun1z.ecommerce.controllers.operacoes;

import io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs.PedidoControllerDocs;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.services.operacoes.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pedidos")
public class PedidoController implements PedidoControllerDocs {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable String id) {
        Pedido obj = service.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<Pedido>> findByCliente(@PathVariable String clienteId, Pageable pageable) {
        Page<Pedido> list = service.listarPorCliente(clienteId, pageable);
        return ResponseEntity.ok(list);
    }

    @GetMapping
    public ResponseEntity<Page<Pedido>> findAll(Pageable pageable) {
        Page<Pedido> list = service.listarTodos(pageable);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/checkout/{clienteId}")
    public ResponseEntity<Pedido> createFromCart(@PathVariable String clienteId) {
        Pedido newObj = service.criarPedidoDoCarrinho(clienteId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(newObj);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> updateStatus(@PathVariable String id, @RequestParam StatusPedido status) {
        Pedido obj = service.atualizarStatus(id, status);
        return ResponseEntity.ok(obj);
    }
}