package io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs;

import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.PagamentoResponseDTO;
import io.github.raphaelmun1z.ecommerce.payment.dto.AbacatePayResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Pagamentos", description = "Endpoints para consulta de pagamentos e geração de links do gateway")
public interface PagamentoControllerDocs {

    @Operation(summary = "Buscar pagamento por ID", description = "Recupera os detalhes de um pagamento específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento encontrado", content = @Content(schema = @Schema(implementation = PagamentoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado", content = @Content)
    })
    ResponseEntity<PagamentoResponseDTO> buscarPorId(
            @Parameter(description = "ID do pagamento") @PathVariable String id
    );

    @Operation(summary = "(Re)gerar link de pagamento", description = "Gera um novo link de pagamento na AbacatePay para um pedido existente. Útil para retentativas ou links expirados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link gerado com sucesso", content = @Content(schema = @Schema(implementation = AbacatePayResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Pedido já está pago ou finalizado", content = @Content)
    })
    ResponseEntity<AbacatePayResponse> criarPagamento(
            @Parameter(description = "ID do pedido") @PathVariable String pedidoId
    );
}