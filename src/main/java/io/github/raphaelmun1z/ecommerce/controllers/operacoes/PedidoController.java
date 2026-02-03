package io.github.raphaelmun1z.ecommerce.controllers.operacoes;

import io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs.PedidoControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.PedidoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.services.operacoes.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/pedidos")
public class PedidoController implements PedidoControllerDocs {
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable String id) {
        PedidoResponseDTO obj = service.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @Override
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<PedidoResponseDTO>> listarPorCliente(@PathVariable String clienteId, Pageable pageable) {
        Page<PedidoResponseDTO> list = service.listarPorCliente(clienteId, pageable);
        return ResponseEntity.ok(list);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<PedidoResponseDTO>> listarTodos(Pageable pageable) {
        Page<PedidoResponseDTO> list = service.listarTodos(pageable);
        return ResponseEntity.ok(list);
    }

    @Override
    @PostMapping("/checkout/{clienteId}")
    public ResponseEntity<PedidoResponseDTO> criarPedidoDoCarrinho(
            @PathVariable String clienteId,
            @RequestParam BigDecimal valorFrete,
            @RequestParam String enderecoId
    ) {
        PedidoResponseDTO newObj = service.criarPedidoDoCarrinho(clienteId, valorFrete, enderecoId);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(newObj);
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable String id, @RequestParam StatusPedido status) {
        PedidoResponseDTO obj = service.atualizarStatus(id, status);
        return ResponseEntity.ok(obj);
    }
}