package io.github.raphaelmun1z.ecommerce.controllers.operacoes;

import io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs.PagamentoControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.PagamentoResponseDTO;
import io.github.raphaelmun1z.ecommerce.payment.dto.AbacatePayResponse;
import io.github.raphaelmun1z.ecommerce.services.operacoes.PagamentoService;
import io.github.raphaelmun1z.ecommerce.services.operacoes.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController implements PagamentoControllerDocs {

    private final PagamentoService pagamentoService;
    private final PedidoService pedidoService;

    public PagamentoController(PagamentoService pagamentoService, PedidoService pedidoService) {
        this.pagamentoService = pagamentoService;
        this.pedidoService = pedidoService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> buscarPorId(@PathVariable String id) {
        PagamentoResponseDTO obj = pagamentoService.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<AbacatePayResponse> criarPagamento(@PathVariable String pedidoId) {
        AbacatePayResponse response = pedidoService.gerarLinkPagamento(pedidoId);
        return ResponseEntity.ok(response);
    }
}