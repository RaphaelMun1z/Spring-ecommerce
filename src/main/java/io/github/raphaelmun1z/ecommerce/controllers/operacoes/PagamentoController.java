package io.github.raphaelmun1z.ecommerce.controllers.operacoes;

import io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs.PagamentoControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.PagamentoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPagamento;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pagamento;
import io.github.raphaelmun1z.ecommerce.services.operacoes.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController implements PagamentoControllerDocs {

    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> buscarPorId(@PathVariable String id) {
        PagamentoResponseDTO obj = service.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @Override
    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<PagamentoResponseDTO> criarPagamento(@PathVariable String pedidoId, @Valid @RequestBody Pagamento pagamento) {
        PagamentoResponseDTO newObj = service.criarPagamento(pagamento, pedidoId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(newObj);
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<PagamentoResponseDTO> atualizarStatus(
        @PathVariable String id,
        @RequestParam StatusPagamento status,
        @RequestParam(required = false) String codigoGateway) {

        PagamentoResponseDTO obj = service.atualizarStatus(id, status, codigoGateway);
        return ResponseEntity.ok(obj);
    }
}