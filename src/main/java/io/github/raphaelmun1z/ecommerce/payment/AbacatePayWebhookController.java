package io.github.raphaelmun1z.ecommerce.payment;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.services.operacoes.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhooks/abacatepay")
public class AbacatePayWebhookController {
    private final PedidoService pedidoService;

    public AbacatePayWebhookController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Void> receberWebhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "abacatepay-signature", required = false) String signature
    ) {
        String event = (String) payload.get("event");

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) payload.get("data");

        if (event == null || data == null) {
            return ResponseEntity.badRequest().build();
        }

        String billingId = (String) data.getOrDefault("id", data.get("_id"));

        if (billingId == null) {
            System.err.println("Webhook ignorado: Billing ID não encontrado.");
            return ResponseEntity.badRequest().build();
        }

        try {
            switch (event) {
                case "billing.paid" -> {
                    pedidoService.confirmarPagamento(billingId, billingId);
                }

                case "billing.failed", "billing.refunded" -> {
                    pedidoService.cancelarPagamento(billingId);
                }

                default -> System.out.println("Evento de Webhook ignorado: " + event);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }
}