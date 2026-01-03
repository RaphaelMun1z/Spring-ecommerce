package io.github.raphaelmun1z.ecommerce.controllers.analitico;

import io.github.raphaelmun1z.ecommerce.controllers.analitico.docs.NotificacaoControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.res.analitico.NotificacaoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.Notificacao;
import io.github.raphaelmun1z.ecommerce.services.analitico.NotificacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController implements NotificacaoControllerDocs {
    private final NotificacaoService service;

    public NotificacaoController(NotificacaoService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<NotificacaoResponseDTO>> listarPorCliente(
        @PathVariable String clienteId,
        Pageable pageable) {

        Page<NotificacaoResponseDTO> list = service.listarPorCliente(clienteId, pageable);
        return ResponseEntity.ok(list);
    }
}