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
        try {
            System.out.println(">>> WEBHOOK RECEBIDO: " + payload);

            String event = (String) payload.get("event");
            Map<String, Object> data = (Map<String, Object>) payload.get("data");

            if (event == null || data == null) {
                return ResponseEntity.badRequest().build();
            }

            Map<String, Object> billing = (Map<String, Object>) data.get("billing");

            if (billing == null) {
                billing = data;
            }

            String billingId = (String) billing.getOrDefault("id", billing.get("_id"));

            if (billingId == null) {
                System.err.println("Webhook ignorado: Billing ID não encontrado no JSON.");
                return ResponseEntity.badRequest().build();
            }

            switch (event) {
                case "billing.paid" -> {
                    System.out.println("Caiu no paid");
                    pedidoService.confirmarPagamento(billingId, billingId);
                }

                case "billing.failed", "billing.refunded" -> {
                    System.out.println("Caiu no failed ou refunded");
                    pedidoService.cancelarPagamento(billingId);
                }

                default -> System.out.println("Evento de Webhook ignorado: " + event);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}